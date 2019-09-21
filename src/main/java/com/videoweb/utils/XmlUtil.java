package com.videoweb.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;
import org.xml.sax.InputSource;

@SuppressWarnings({ "rawtypes", "unchecked" })
public final class XmlUtil {
	private static Log log = LogFactory.getLog(XmlUtil.class);

	private XmlUtil() {
	}

	/**
	 * 解析XML并将其节点元素压入Dto返回(基于节点值形式的XML格式)
	 * 
	 * @param pStrXml 待解析的XML字符串
	 * @return outDto 返回Dto
	 */
	public static final Map parseXml2Map(String pStrXml) {
		Map map = new HashMap();
		String strTitle = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		Document document = null;
		try {
			if (pStrXml.indexOf("<?xml") < 0)
				pStrXml = strTitle + pStrXml;
			document = DocumentHelper.parseText(pStrXml);

		} catch (DocumentException e) {
			log.error("==开发人员请注意:==\n将XML格式的字符串转换为XML DOM对象时发生错误啦!" + "\n详细错误信息如下:", e);
		}
		// 获取根节点
		Element elNode = document.getRootElement();
		// 遍历节点属性值将其压入Dto
		for (Iterator it = elNode.elementIterator(); it.hasNext();) {
			Element leaf = (Element) it.next();
			map.put(leaf.getName().toLowerCase(), leaf.getData());
		}
		return map;
	}

	/**
	 * 解析XML并将其节点元素压入Dto返回(基于节点值形式的XML格式) 应用于复杂对象
	 * 
	 * @param pStrXml 待解析的XML字符串
	 * @return outDto 返回Dto
	 */

	public static Map Dom2Map(Document doc) {
		Map map = new HashMap();
		if (doc == null)
			return map;
		Element root = doc.getRootElement();
		for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
			Element e = (Element) iterator.next();
			// System.out.println(e.getName());
			List list = e.elements();
			if (list.size() > 0) {
				map.put(e.getName(), Dom2Map(e));
			} else
				map.put(e.getName(), e.getText());
		}
		return map;
	}

	public static Map Dom2Map(Element e) {
		Map map = new HashMap();
		List list = e.elements();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Element iter = (Element) list.get(i);
				List mapList = new ArrayList();

				if (iter.elements().size() > 0) {
					Map m = Dom2Map(iter);
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(m);
						}
						if (obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = (List) obj;
							mapList.add(m);
						}
						map.put(iter.getName(), mapList);
					} else
						map.put(iter.getName(), m);
				} else {
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(iter.getText());
						}
						if (obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = (List) obj;
							mapList.add(iter.getText());
						}
						map.put(iter.getName(), mapList);
					} else
						map.put(iter.getName(), iter.getText());
				}
			}
		} else
			map.put(e.getName(), e.getText());
		return map;
	}

	/**
	 * 解析XML并将其节点元素压入Dto返回(基于节点值形式的XML格式)
	 * 
	 * @param pStrXml 待解析的XML字符串
	 * @param pXPath 节点路径(例如："//paralist/row" 则表示根节点paralist下的row节点的xPath路径)
	 * @return outDto 返回Dto
	 */
	public static final Map parseXml2Map(String pStrXml, String pXPath) {
		Map map = new HashMap();
		String strTitle = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		Document document = null;
		try {
			if (pStrXml.indexOf("<?xml") < 0)
				pStrXml = strTitle + pStrXml;
			document = DocumentHelper.parseText(pStrXml);
		} catch (DocumentException e) {
			log.error("==开发人员请注意:==\n将XML格式的字符串转换为XML DOM对象时发生错误啦!" + "\n详细错误信息如下:", e);
		}
		// 获取根节点
		Element elNode = document.getRootElement();
		// 遍历节点属性值将其压入Dto
		for (Iterator it = elNode.elementIterator(); it.hasNext();) {
			Element leaf = (Element) it.next();
			map.put(leaf.getName().toLowerCase(), leaf.getData());
		}
		return map;
	}

	/**
	 * 将Dto转换为符合XML标准规范格式的字符串(基于节点值形式)
	 * 
	 * @param dto 传入的Dto对象
	 * @param pRootNodeName 根结点名
	 * @return string 返回XML格式字符串
	 */
	public static final String parseDto2Xml(Map map, String pRootNodeName) {
		Document document = DocumentHelper.createDocument();
		// 增加一个根元素节点
		document.addElement(pRootNodeName);
		Element root = document.getRootElement();
		Iterator keyIterator = map.keySet().iterator();
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next();
			String value = (String) map.get(key);
			Element leaf = root.addElement(key);
			leaf.setText(value);
		}
		// 将XML的头声明信息截去
		String outXml = document.asXML().substring(39);
		return outXml;
	}

	/**
	 * 将Dto转换为符合XML标准规范格式的字符串(基于节点值形式)
	 * 
	 * @param dto 传入的Dto对象
	 * @param pRootNodeName 根结点名
	 * @return string 返回XML格式字符串
	 */
	public static final String parseDto2XmlHasHead(Map map, String pRootNodeName) {
		Document document = DocumentHelper.createDocument();
		// 增加一个根元素节点
		document.addElement(pRootNodeName);
		Element root = document.getRootElement();
		Iterator keyIterator = map.keySet().iterator();
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next();
			String value = (String) map.get(key);
			Element leaf = root.addElement(key);
			leaf.setText(value);
		}
		// 将XML的头声明信息截去
		// String outXml = document.asXML().substring(39);
		String outXml = document.asXML();
		return outXml;
	}

	/**
	 * 将Dto转换为符合XML标准规范格式的字符串(基于属性值形式)
	 * 
	 * @param map 传入的Dto对象
	 * @param pRootNodeName 根节点名
	 * @param pFirstNodeName 一级节点名
	 * @return string 返回XML格式字符串
	 */
	public static final String parseMap2Xml(Map map, String pRootNodeName, String pFirstNodeName) {
		Document document = DocumentHelper.createDocument();
		// 增加一个根元素节点
		document.addElement(pRootNodeName);
		Element root = document.getRootElement();
		root.addElement(pFirstNodeName);
		Element firstEl = (Element) document.selectSingleNode("/" + pRootNodeName + "/" + pFirstNodeName);
		Iterator keyIterator = map.keySet().iterator();
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next();
			String value = (String) map.get(key);
			firstEl.addAttribute(key, value);
		}
		// 将XML的头声明信息丢去
		String outXml = document.asXML().substring(39);
		return outXml;
	}

	/**
	 * 将List数据类型转换为符合XML格式规范的字符串(基于节点属性值的方式)
	 * 
	 * @param pList 传入的List数据(List对象可以是Dto、VO、Domain的属性集)
	 * @param pRootNodeName 根节点名称
	 * @param pFirstNodeName 行节点名称
	 * @return string 返回XML格式字符串
	 */
	public static final String parseList2Xml(List pList, String pRootNodeName, String pFirstNodeName) {
		Document document = DocumentHelper.createDocument();
		Element elRoot = document.addElement(pRootNodeName);
		for (int i = 0; i < pList.size(); i++) {
			Map map = (Map) pList.get(i);
			Element elRow = elRoot.addElement(pFirstNodeName);
			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				elRow.addAttribute((String) entry.getKey(), String.valueOf(entry.getValue()));
			}
		}
		String outXml = document.asXML().substring(39);
		return outXml;
	}

	/**
	 * 将List数据类型转换为符合XML格式规范的字符串(基于节点值的方式)
	 * 
	 * @param pList 传入的List数据(List对象可以是Dto、VO、Domain的属性集)
	 * @param pRootNodeName 根节点名称
	 * @param pFirstNodeName 行节点名称
	 * @return string 返回XML格式字符串
	 */
	public static final String parseList2XmlBasedNode(List pList, String pRootNodeName, String pFirstNodeName) {
		Document document = DocumentHelper.createDocument();
		Element output = document.addElement(pRootNodeName);
		for (int i = 0; i < pList.size(); i++) {
			Map map = (Map) pList.get(i);
			Element elRow = output.addElement(pFirstNodeName);
			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				Element leaf = elRow.addElement((String) entry.getKey());
				leaf.setText(String.valueOf(entry.getValue()));
			}
		}
		String outXml = document.asXML().substring(39);
		return outXml;
	}

	/**
	 * 将XML规范的字符串转为List对象(XML基于节点属性值的方式)
	 * 
	 * @param pStrXml 传入的符合XML格式规范的字符串
	 * @return list 返回List对象
	 */
	public static final List parseXml2List(String pStrXml) {
		List lst = new ArrayList();
		String strTitle = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		Document document = null;
		try {
			if (pStrXml.indexOf("<?xml") < 0)
				pStrXml = strTitle + pStrXml;
			document = DocumentHelper.parseText(pStrXml);
		} catch (DocumentException e) {
			log.error("==开发人员请注意:==\n将XML格式的字符串转换为XML DOM对象时发生错误啦!" + "\n详细错误信息如下:", e);
		}
		// 获取到根节点
		Element elRoot = document.getRootElement();
		// 获取根节点的所有子节点元素
		Iterator elIt = elRoot.elementIterator();
		while (elIt.hasNext()) {
			Element el = (Element) elIt.next();
			Iterator attrIt = el.attributeIterator();
			Map map = new HashMap();
			while (attrIt.hasNext()) {
				Attribute attribute = (Attribute) attrIt.next();
				map.put(attribute.getName().toLowerCase(), attribute.getData());
			}
			lst.add(map);
		}
		return lst;
	}

	/**
	 * Document to map
	 * 
	 * @param doc
	 * @return
	 */
	public static Map<String, Object> dom2Map(Document doc) {
		Map<String, Object> maproot = new HashMap<String, Object>();
		if (doc == null)
			return maproot;
		Element root = doc.getRootElement();

		List list1 = root.elements();
		for (Object obj : list1) {
			Element element = (Element) obj;
			Map<String, Object> map = new HashMap<String, Object>();
			element2Map(element, map);
			maproot.put(element.getName(), map);
		}
		return maproot;
	}

	/**
	 * Element to map
	 * 
	 * @param e
	 * @return
	 */
	public static void element2Map(Element e, Map<String, Object> map) {
		List<Object> list = e.elements();
		if (e.attributeCount() > 0) {
			for (Object attri : e.attributes()) {
				Attribute at = (Attribute) attri;
				map.put(at.getName(), at.getValue());
			}
		}
		if (list.size() < 1 && DataUtil.isEmpty(e.getText())) {
			return;
		} else if (list.size() < 1 && !DataUtil.isEmpty(e.getText())) {
			map.put("text", e.getText());
		}
		for (Object aList : list) {
			Element iter = (Element) aList;
			Map<String, Object> cMap = new HashMap<String, Object>();
			element2Map(iter, cMap);
			map.put(iter.getName(), cMap);
		}
	}

	/**
	 * 将mq查询结果包装成list--dto的形式，dto内容为item中的内容
	 * 
	 * @param recv
	 * @return
	 */
	public static Map MqResToDto(String recv) {
		// System.out.println("####recv"+recv);
		List res = new ArrayList();
		Map map = new HashMap();
		try {
			Document doc = DocumentHelper.parseText(recv);
			List list = doc.selectNodes("//item");
			Iterator<DefaultElement> it = list.iterator();
			while (it.hasNext()) {
				Map elementdto = XmlUtil.Dom2Map(it.next());
				res.add(elementdto);
			}
			map.put("resultList", res);// 放入结果集
			/*
			 * 如果存在REC_MNT，说明是分页查询类，需要将总记录数返回
			 */
			Node de = doc.selectSingleNode("//REC_MNT");
			if (DataUtil.isNotEmpty(de)) {
				map.put("countInteger", de.getText());
			}
		} catch (Exception e) {
			log.error(XmlUtil.class, e);
		}
		return map;
	}
	
	/**
     * 从request中获得参数Map，并返回可读的Map
     * 
     * @param request
     * @return
     */
    public static SortedMap getParameterMap(HttpServletRequest request) {
        // 参数Map
        Map properties = request.getParameterMap();
        // 返回值Map
        SortedMap returnMap = new TreeMap();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if(null == valueObj){
                value = "";
            }else if(valueObj instanceof String[]){
                String[] values = (String[])valueObj;
                for(int i=0;i<values.length;i++){
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length()-1);
            }else{
                value = valueObj.toString();
            }
            returnMap.put(name, value.trim());
        }
        return returnMap;
    }

    public static String parseXML(SortedMap<String, String> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if (null != v && !"".equals(v) && !"appkey".equals(k)) {
                sb.append("<" + k + ">" + parameters.get(k) + "</" + k + ">\n");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }
    /** 获取流转字符串
     * <功能详细描述>request转字符串
     * @param request
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String parseRequst(HttpServletRequest request){
        String body = "";
        try {
            ServletInputStream inputStream = request.getInputStream(); 
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            while(true){
                String info = br.readLine();
                if(info == null){
                    break;
                }
                if(body == null || "".equals(body)){
                    body = info;
                }else{
                    body += info;
                }
            }
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
        	log.error(e.getMessage());
        }            
        return body;
    }
    /**
     * 转XMLmap
     * @author  
     * @param xmlBytes
     * @param charset
     * @return
     * @throws Exception
     */
    public static Map<String, String> toMap(byte[] xmlBytes,String charset) throws Exception{
        SAXReader reader = new SAXReader(false);
        InputSource source = new InputSource(new ByteArrayInputStream(xmlBytes));
        source.setEncoding(charset);
        Document doc = reader.read(source);
        Map<String, String> params = XmlUtil.toMap(doc.getRootElement());
        return params;
    }
    /**
     * 转MAP
     * @author  
     * @param element
     * @return
     */
    public static Map<String, String> toMap(Element element){
        Map<String, String> rest = new HashMap<String, String>();
        List<Element> els = element.elements();
        for(Element el : els){
            rest.put(el.getName().toLowerCase(), el.getTextTrim());
        }
        return rest;
    }
    
    /**
     * maptoxml
     * @param params
     * @return
     */
    public static String toXml2(Map<String, Object> params){
        StringBuilder buf = new StringBuilder();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        buf.append("<xml>");
        for(String key : keys){
            buf.append("<").append(key).append(">");
            buf.append("<![CDATA[").append(params.get(key)).append("]]>");
            buf.append("</").append(key).append(">\n");
        }
        buf.append("</xml>");
        return buf.toString();
    }
    /**
     * maptoxml
     * @param params
     * @return
     */
    public static String toXml(Map<String, String> params){
        StringBuilder buf = new StringBuilder();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        buf.append("<xml>");
        for(String key : keys){
            buf.append("<").append(key).append(">");
            buf.append("<![CDATA[").append(params.get(key)).append("]]>");
            buf.append("</").append(key).append(">\n");
        }
        buf.append("</xml>");
        return buf.toString();
    }
    public static Map<String, Object> ConvertObjToMap(Object obj) {  
        Map<String, Object> reMap = new HashMap<String, Object>();  
        if (obj == null)  
            return null;  
        Field[] fields = obj.getClass().getDeclaredFields();  
        try {  
            for (int i = 0; i < fields.length; i++) {  
                try {  
                    Field f = obj.getClass().getDeclaredField(  
                            fields[i].getName());  
                    f.setAccessible(true);  
                    Object o = f.get(obj);  
                    if(o!=null&&o!=""){
                    	if(!fields[i].getName().equals("serialVersionUID")){
                    		reMap.put(fields[i].getName(), o);  
                    	}
                    }
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        } catch (SecurityException e) {  
            e.printStackTrace();  
        }  
        return reMap;  
    } 
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {    
        if (map == null)  
            return null;  
  
        Object obj = beanClass.newInstance();  
  
        org.apache.commons.beanutils.BeanUtils.populate(obj, map);  
  
        return obj;  
    }    
      
    public static Map<?, ?> objectToMap(Object obj) {  
        if(obj == null)  
            return null;   
  
        return new org.apache.commons.beanutils.BeanMap(obj);  
    }
    public static Map<String, Object> xmlToMap(String xml) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Document doc = DocumentHelper.parseText(xml);
			//修复微信XEE漏洞 https://security.tencent.com/index.php/blog/msg/69
			doc.setEntityResolver(new IgnoreDTDEntityResolver());
			Element root = doc.getRootElement();
			List<Element> elements = root.elements();
			for (Element element : elements) {
				map.put(element.getName(), element.getText());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return map;
	}

	public static Document getDocument(Object b) {
		Document document = DocumentHelper.createDocument();
		try {
			// 创建根节点元素
			Element root = document.addElement(b.getClass().getSimpleName());
			Field[] field = b.getClass().getDeclaredFields(); // 获取实体类b的所有属性，返回Field数组
			for (int j = 0; j < field.length; j++) { // 遍历所有有属性
				String name = field[j].getName(); // 获取属属性的名字
				if (!name.equals("serialVersionUID")) {// 去除串行化序列属性
					name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
					Method m = b.getClass().getMethod("get" + name);
					// System.out.println("属性get方法返回值类型:" + m.getReturnType());
					String propertievalue = (String) m.invoke(b);// 获取属性值
					Element propertie = root.addElement(name);
					propertie.setText(propertievalue);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return document;
	}

	/**
	* 对象转xml格式的字符串
	* @param b
	* @return
	*/
	public static String getXmlString(Object b) {
		return getDocument(b).asXML();
	}
    
    public static void main(String[] args) {
		String s = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg><appid><![CDATA[wx2421b1c4370ec43b]]></appid><mch_id><![CDATA[10000100]]></mch_id><nonce_str><![CDATA[IITRi8Iabbblz1Jc]]></nonce_str><sign><![CDATA[7921E432F65EB8ED0CE9755F0E86D72F]]></sign><result_code><![CDATA[SUCCESS]]></result_code><prepay_id><![CDATA[wx201411101639507cbf6ffd8b0779950874]]></prepay_id><trade_type><![CDATA[APP]]></trade_type></xml>";
		Map<String, Object> e = xmlToMap(s);
		for (String w : e.keySet()) {
			System.out.println(w);
		}
    }
}
package RemoveUnuseResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * 删除没用的资源
 */
public class RemoveUnuseResource {

    private static String[] resPaths = {
//            "./PeekabooAndroid/app/src/main/res/values/strings.xml",
//            "./PeekabooAndroid/app/src/main/res/values-zh-rCN/strings.xml",
//            "./PeekabooAndroid/app/src/main/res/values-ja-rJP/strings.xml",
//            "./PeekabooAndroid/app/src/main/res/values-zh/strings.xml",
//            "./PeekabooAndroid/app/src/main/res/values/colors.xml"
    };

    private static String detectFolderBehind = "app/src/main/res";
    private static String folderFrontAdd = "values";
    private static String deletePath;
    private static Document xmldoc;
    private static Element root;
    static String projectPath = "./PeekabooAndroid/";


    /**
     * 选这个node
     * @param express
     * @param source
     * @return
     */
    public static Node selectSingleNode(String express, Element source) {
        Node result=null;
        XPathFactory xpathFactory=XPathFactory.newInstance();
        XPath xpath=xpathFactory.newXPath();
        try {
            result=(Node) xpath.evaluate(express, source, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        
        return result;
    }

    /**
     * 删除这个 node
     * @param express
     * @param name
     */
    public static void discardSon(String express, String name){
        try{
            name.trim();
            Element stringItem =(Element) selectSingleNode(express + "[@name='"+ name + "']", root);
            Node removeNode = root.removeChild(stringItem);
            System.out.println("删除这个item " + name + " node = " + removeNode);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        if (args != null && args.length > 0) {
            projectPath = args[0];
        }
        for (String resPath :
                resPaths) {
            processFile(resPath);
        }
        File detectFolderFile = new File(projectPath + detectFolderBehind);
        File[] detectFolders = null;
        File[] detectFiles = null;
        if (detectFolderFile.exists() && (detectFolders = detectFolderFile.listFiles()) != null) {
            for (File folder : detectFolders) {
                if (folder.getName().indexOf(folderFrontAdd) >=0) {
                    System.out.println("监测这个文件夹里面的 = " + folder.getName());
                    if (folder.exists() && folder.isDirectory()
                            && (detectFiles=folder.listFiles()) != null) {
                        for (File file : detectFiles) {
                            processFile(file.getPath());
                        }
                    }
                }
            }
        }
    }

    /**
     * 初始化
     */
    public static void initDocument(String resPath){
        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
        dbf.setIgnoringElementContentWhitespace(true);
        NodeList list = null;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            xmldoc = db.parse(resPath); // 使用dom解析xml文件
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void processFile (String resPath) {
        System.out.println("开始处理这个文件 = " + resPath);
        try{
            initDocument(resPath);
            root = xmldoc.getDocumentElement();
            removeStringItem();

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer former = factory.newTransformer();
            former.transform(new DOMSource(xmldoc), new StreamResult(new File(resPath)));
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("处理这个文件exception = " + e.getMessage());
        }
    }

    public static void removeStringItem() {

        String path = "./RemoveUnuseResource/lint-results.xml";
        path = "./RemoveUnuseResource/AndroidLintUnusedResources.xml";

        File f = new File(path);
        File file = new File(path);

        if (!file.exists() || file.isDirectory()) {
            System.out.println("AndroidLintUnusedResources exists= " + file.exists());
            System.out.println("AndroidLintUnusedResources .isDirectory()= " + file.isDirectory());
            return;
        }

        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document doc = db.parse(new File(path));
            //获得根元素结点
            Element root = doc.getDocumentElement();

            parseElement(root);
        } catch (Exception e) {

        }

    }

    public static void parseElement(Element element) {
        String tagName = element.getNodeName();

        NodeList children = element.getChildNodes();

        // System.out.print("\n tagName = " + tagName);

        //element元素的所有属性所构成的NamedNodeMap对象，需要对其进行判断
        NamedNodeMap map = element.getAttributes();

        //如果该元素存在属性
        if (null != map) { 
            if (true) {
                for (int nodeI = 0; nodeI < children.getLength(); nodeI++) {
                    Node node = children.item(nodeI);
                    short nodeType = node.getNodeType();
                    //获得结点的类型
                    if (nodeType == Node.ELEMENT_NODE) {
                        //是元素，继续递归
                        Element tmpElement = ((Element) node);
                        String tmpTagName = tmpElement.getNodeName();
                        if ("problem".equalsIgnoreCase(tmpTagName)) {

                            // System.out.println("这是为使用的，location的  = " + tmpTagName);

                            NodeList problemChild = tmpElement.getChildNodes();

                            for (int i = 0; i < problemChild.getLength(); i++) {
                                //获得该元素的每一个属性
                                Node fileNode = problemChild.item(i);

                                // System.out.println(problemChild.getLength() + "  " + i + "fileNode = " + fileNode.getNodeName());

                                String fileNodeName = fileNode.getNodeName();

                                // System.out.println(i + "fileNodeName = " 
                                //     + fileNodeName);

                                if (deleteStringItem(fileNode, fileNodeName)) {
                                    // nothing
                                } else if (deleteFileItem(fileNode, fileNodeName)) {
                                    // nothing
                                }
                            }
                        }
                    }
                }
            } else {
                for (int i = 0; i < children.getLength(); i++) {
                    Node node = children.item(i);
                    //获得结点的类型
                    short nodeType = node.getNodeType();
                    if (nodeType == Node.ELEMENT_NODE) {
                        //是元素，继续递归
                        parseElement((Element) node);
                    }
                }
            }
        }
    }

    public static boolean deleteFileItem (Node fileNode, String fileNodeName) {
        if ("file".equalsIgnoreCase(fileNodeName)) {
            //有id的话就继续。
            String fileNodeValue = fileNode.getTextContent();
            String fileRow = fileNodeValue;
            System.out.println("file = " + fileRow);
            if ((fileRow.contains("res/drawable")
                    || fileRow.contains("res/layout")
                    || fileRow.contains("res/anim")
                    || fileRow.contains("res/menu")
                    || fileRow.contains("res/raw")
                    || fileRow.contains("res/color"))) {

                String delFile = fileRow.replace("file://$PROJECT_DIR$/", projectPath);

                System.out.println("删除这个无用文件 -- " + delFile
                        + " 删除前在不在 = " + new File(delFile).exists()
                        + " 删除是否成功 = " + new File(delFile).delete()
                        + " 删除后在不在 = " + new File(delFile).exists()); //+ new File(delFile).delete()
                return true;
            }
        }
        return false;
    }

    static String unuse_behind = "</code> appears to be unused</html>";
    
    public static boolean deleteStringItem (Node fileNode, String fileNodeName) {
        if ("description".equalsIgnoreCase(fileNodeName)) {
            //有id的话就继续。
            String fileNodeValue = fileNode.getTextContent();
            String fileRow = fileNodeValue;
            if (fileRow.contains("R.plurals.")
                    && fileRow.contains("appears to be unused")) {
                String delStringName = fileRow.substring(fileRow.indexOf("R.plurals.") + "R.plurals.".length(),
                        fileRow.indexOf(unuse_behind));
                System.out.println("删除这个 plurals = " + delStringName);
                discardSon("/resources/plurals", delStringName);
                return true;
            } else if (fileRow.contains("R.string.")
                    && fileRow.contains("appears to be unused")) {
                String delStringName = fileRow.substring(fileRow.indexOf("R.string.") + "R.string.".length(),
                        fileRow.indexOf(unuse_behind));
                System.out.println("删除这个 string = " + delStringName);
                discardSon("/resources/string", delStringName);
                return true;
            } else if (fileRow.contains("R.color.")
                    && fileRow.contains("appears to be unused")) {
                String delStringName = fileRow.substring(fileRow.indexOf("R.color.") + "R.color.".length(),
                        fileRow.indexOf(unuse_behind));
                System.out.println("删除这个 color = " + delStringName);
                discardSon("/resources/color", delStringName);
                discardSon("/resources/item", delStringName);
                return true;
            } else if (fileRow.contains("R.array.")
                    && fileRow.contains("appears to be unused")) {
                String delStringName = fileRow.substring(fileRow.indexOf("R.array.") + "R.array.".length(),
                        fileRow.indexOf(unuse_behind));
                System.out.println("删除这个 string-array = " + delStringName);
                discardSon("/resources/array", delStringName);
                discardSon("/resources/string-array", delStringName);
                discardSon("/resources/item", delStringName);
                return true;
            } else if (fileRow.contains("R.dimen.")
                    && fileRow.contains("appears to be unused")) {
                String delStringName = fileRow.substring(fileRow.indexOf("R.dimen.") + "R.dimen.".length(),
                        fileRow.indexOf(unuse_behind));
                System.out.println("删除这个 dimen = " + delStringName);
                discardSon("/resources/dimen", delStringName);
                discardSon("/resources/item", delStringName);
                return true;
            }
        }
        return false;
    }

}
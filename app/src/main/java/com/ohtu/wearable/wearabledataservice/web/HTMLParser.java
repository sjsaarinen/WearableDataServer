package com.ohtu.wearable.wearabledataservice.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;


public class HTMLParser {

    private String[] texts=new String[]{"span"};
    private String[] buttons=new String[]{"button"};

    public HTMLParser(){

    }

    public List<WebElement> parseHTML(String html){
        List<WebElement> webElements =new ArrayList<>();
        Source source=new Source(html);
        List<Element> headElementList=source.getAllElements(HTMLElementName.HEAD);
        List<Element> bodyElementList=source.getAllElements(HTMLElementName.BODY);
        if(headElementList.size()==1){
            //webElements.addAll(this.toElements(headElementList.get(0)));
        }
        if(bodyElementList.size()==1){
            webElements.addAll(this.toElements(bodyElementList.get(0)));
        }
        return webElements;
    }

    private String type(Element e){
        String eName=e.getName();
        if(Arrays.asList(texts).contains(eName)){
            return "text";
        }
        else if(Arrays.asList(buttons).contains(eName)){
            return "button";
        }
        return null;
    }

    private List<WebElement> toElements(Element element){
        List<WebElement> webElements =new ArrayList<>();
        List<Element> elementList=element.getChildElements();
        for(Element e:elementList){
            String type=this.type(e);
            if(type!=null) {
                String content = e.getContent().toString();
                WebElement elementView = new WebElement(type, content);
                webElements.add(elementView);
            }
        }
        return webElements;
    }

}

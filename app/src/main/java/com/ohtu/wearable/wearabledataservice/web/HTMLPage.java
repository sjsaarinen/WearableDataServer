package com.ohtu.wearable.wearabledataservice.web;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HTMLPage {

    private List<WebElement> webElements;
    private HTMLParser htmlParser;
    private String html;
    private LinearLayout linearLayout;

    public HTMLPage(LinearLayout linearLayout, String html){
        this.webElements =new ArrayList<>();
        this.htmlParser=new HTMLParser();
        this.linearLayout=linearLayout;
        this.html=html;
        this.parseHTML();
    }

    private void addTextView(WebElement webElement){
        TextView textView=new TextView(this.linearLayout.getContext());
        textView.setText(webElement.getContent());
        this.linearLayout.addView(textView);
    }

    private void addButton(WebElement webElement){
        Button button=new Button(this.linearLayout.getContext());
        button.setText(webElement.getContent());
        this.linearLayout.addView(button);
    }

    public void renderHtmlPage(HTMLPage htmlPage){
        List<WebElement> elements=htmlPage.getWebElements();
        for(WebElement e:elements){
            if(e.getType().equals("text")){
                addTextView(e);
            }
            if(e.getType().equals("button")){
                addButton(e);
            }
        }
    }

    private void parseHTML(){
        this.webElements =this.htmlParser.parseHTML(this.html);
    }

    public List<WebElement> getWebElements(){
        return this.webElements;
    }

}

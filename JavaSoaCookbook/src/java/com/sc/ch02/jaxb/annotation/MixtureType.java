package com.sc.ch02.jaxb.annotation;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

@XmlRootElement
public class MixtureType {
    private Map<QName,Object> any;
    private String title;

    public MixtureType(){}

    @XmlAnyAttribute
    public Map<QName,Object> getAny(){
        if( any == null ){
            any = new HashMap<QName,Object>();
        }
        return any;
    }

    @XmlElement
    public String getTitle(){
        return title;
    }
    public void setTitle( String value ){
        title = value;
    }
}


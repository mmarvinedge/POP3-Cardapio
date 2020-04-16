package com.popsales.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public abstract class AbstractConverter implements Converter {

    private Map<Object, String> entities = new HashMap<Object, String>();

    public AbstractConverter(Map<Object, String> entities) {
        this.entities = entities;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object entity) {
        if(entity == null){
            return "";
        }
        if (!entities.containsKey(entity)) {
            String uuid = UUID.randomUUID().toString();
            entities.put(entity, uuid);
            return uuid;
        } else {
            return entities.get(entity);
        }
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,String uuid) {

        for (Entry<Object, String> entry : entities.entrySet()) {
            if (entry.getValue().equals(uuid)) {
                return entry.getKey();
            }
        }
        return null;
    }
}

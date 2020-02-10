package com.watch.BIOServer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebContext {
	private List<Entity> entitys = null;
	private List<Mapping> mappings = null;

	private Map<String, String> entityMap = new HashMap<>();
	private Map<String, String> mappingMap = new HashMap<>();

	public WebContext(List<Entity> entitys, List<Mapping> mappings) {
		this.entitys = entitys;
		this.mappings = mappings;
		// 将entity的List转成map
		for (Entity entity : entitys) {
			entityMap.put(entity.getName(), entity.getClz());
		}
		// 将mapping的List转成map
		for (Mapping mapping : mappings) {
			for (String pattern : mapping.getPatterns()) {
				mappingMap.put(pattern, mapping.getName());
			}
		}
	}

	
	
	//通过url的路径找到对应的class
	public String getClz(String pattern) {
		String name=mappingMap.get(pattern);
		return entityMap.get(name);
	}
}

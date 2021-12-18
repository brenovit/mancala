package com.bol.brenovit.mancala.core.common.mapper;

import java.util.List;

import lombok.experimental.UtilityClass;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@UtilityClass
public class CustomObjectMapper {
	
	private static final MapperFactory mapperFactory;
		
	static {
		mapperFactory = new DefaultMapperFactory.Builder().build();
	}

	public static <T> T map(Object source, Class<T> destination) {
		return mapperFactory.getMapperFacade().map(source, destination);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> map(@SuppressWarnings("rawtypes") List source, Class<T> destination) {
		return mapperFactory.getMapperFacade().mapAsList(source, destination);
	}

}

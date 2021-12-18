package com.bol.brenovit.mancala.core.game.mapper;

import java.util.List;

import com.bol.brenovit.mancala.core.game.dto.Game;
import com.bol.brenovit.mancala.entrypoint.game.dto.GameResponse;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class GameMapper {

	private static final MapperFactory mapperFactory;

	static {
		mapperFactory = new DefaultMapperFactory.Builder()
				.mapNulls(false)
				.build();

		mapperFactory.classMap(GameResponse.class, Game.class)
				.byDefault()
				.register();
	}

	private GameMapper() {
	}

	public static GameResponse map(Game source) {
		return mapperFactory.getMapperFacade().map(source, GameResponse.class);
	}

	public static List<GameResponse> map(List<Game> source) {
		return mapperFactory.getMapperFacade().mapAsList(source, GameResponse.class);
	}

}
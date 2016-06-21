package com.planed.ctlBot.domain;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.StreamUtils;
import org.springframework.stereotype.Repository;

import com.planed.ctlBot.common.GameStatus;
import com.planed.ctlBot.data.LineupEntity;
import com.planed.ctlBot.data.repositories.LineupEntityRepository;

/**
 * 
 * @author azthec
 *
 */
@Repository
public class LineupRepository { //couple of functions and how to save object from memory to db
	    private final Mapper mapper = new DozerBeanMapper();
	    @Autowired
	    private LineupEntityRepository lineupEntityRepository;
	    
	    
	    public List<Lineup> findAll() {
	        final List<Lineup> result = new ArrayList<Lineup>();
	        StreamUtils.createStreamFromIterator(lineupEntityRepository.findAll().iterator())
	                .map(o -> mapFromEntity(o))
	                .forEach(result::add);
	        return result;
	    }
	    
	    public void addLineup(final String lineupId) {
	    	final Lineup lineup = new Lineup(lineupId);
	    	lineupEntityRepository.save(mapToEntity(lineup));
	    }
	    
	    public void addLineup(final String lineupId, String mentions, String message) {
	    	final Lineup lineup = new Lineup(lineupId, mentions, message);
	    	lineupEntityRepository.save(mapToEntity(lineup));
	    }
	    
	    public Lineup findByLineupId(final String lineupId) {
	        final LineupEntity lineupEntity = lineupEntityRepository.findOne(lineupId);
	        if (lineupEntity == null) {
	            return null;
	        }
	        return mapFromEntity(lineupEntity);
	    }

	    public void save(final Lineup... lineups) {
	        for (final Lineup lineup : lineups) {
	            final LineupEntity lineupEntity = mapToEntity(lineup);
	            lineupEntityRepository.save(lineupEntity);
	        }
	    }

	    private LineupEntity mapToEntity(final Lineup lineup) {
	        final LineupEntity result = mapper.map(lineup, LineupEntity.class);
	        result.setLineupId(lineup.getLineupId());
	        return result;
	    }

	    private Lineup mapFromEntity(final LineupEntity lineup) {
	        final Lineup result = mapper.map(lineup, Lineup.class);
	        return result;
	    }

	    public Lineup refresh(final Lineup lineup) {
	        return mapFromEntity(lineupEntityRepository.findOne(lineup.getLineupId()));
	    }
	    
	    public void removeLineup(final String lineupId) {
			lineupEntityRepository.delete(lineupId);
		}
}

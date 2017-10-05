package com.github.lindenb.biospringbatch.bio;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.core.io.Resource;

public class FastaSequenceReader 
	implements 
		ItemStreamReader<FastaSequence>,
		ResourceAwareItemReaderItemStream<FastaSequence>{
	private Resource resource = null;
	private PushbackReader reader = null;
	private boolean at_begin=true;
	
	@Override
	public void setResource(final Resource resource) {
		this.resource = resource; 
		}
	
	@Override
	public void open(final ExecutionContext arg0) throws ItemStreamException {
		try {
			at_begin=true;
			this.reader = new PushbackReader(new InputStreamReader(this.resource.getInputStream(),"UTF-8"));
		} catch (IOException err) {
			throw new ItemStreamException(err);
		}
		
	}

	
	@Override
	public void close() throws ItemStreamException {
		try {
			this.reader.close();
			at_begin=true;
		} catch (final IOException err) {
			throw new ItemStreamException(err);
		}
	}


	@Override
	public void update(ExecutionContext arg0) throws ItemStreamException {
	}

	@Override
	public FastaSequence read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		StringBuilder name= null;
		StringBuilder seq= null;
		int c=this.reader.read();
		
		return FastaSequence.from(name.toString(),seq.toString());
		
	}

}

package com.github.lindenb.biospringbatch.bio;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.core.io.Resource;

import htsjdk.samtools.util.CloseableIterator;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;

public class VariantListReader 	implements 
	ItemStreamReader<List<VariantContext>>,
	ResourceAwareItemReaderItemStream<List<VariantContext>>{
	private static final Log logger = LogFactory.getLog(VariantListReader.class);	
	private Resource resource = null;
	private VCFFileReader vcfFileReader = null;
	private CloseableIterator<VariantContext> iter=null;
	

	@Override
	public void open(final ExecutionContext arg0) throws ItemStreamException {
		try {
			this.vcfFileReader = new VCFFileReader(
					Objects.requireNonNull(this.resource).getFile(),
					false
					);
			this.iter = this.vcfFileReader.iterator();
			}
		catch(IOException err) {
			throw new ItemStreamException(err);
			}
		
	}

	@Override
	public void close() throws ItemStreamException {
		if(this.iter!=null) this.iter.close();
		if(this.vcfFileReader!=null) this.vcfFileReader.close();
	}

	@Override
	public void update(ExecutionContext arg0) throws ItemStreamException {
		logger.debug("update");
	}

	@Override
	public List<VariantContext> read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		return this.iter.hasNext()?
				Collections.singletonList(iter.next()):
				null;
		}

	@Override
	public void setResource(final Resource rsrc) {
		this.resource = rsrc;
		
	}


}

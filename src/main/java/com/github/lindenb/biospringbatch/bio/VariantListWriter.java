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
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.file.ResourceAwareItemWriterItemStream;
import org.springframework.core.io.Resource;

import htsjdk.samtools.util.CloseableIterator;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.variantcontext.writer.VariantContextWriterBuilder;
import htsjdk.variant.vcf.VCFFileReader;

public class VariantListWriter 	implements 
	ItemStreamWriter<List<VariantContext>>,
	ResourceAwareItemWriterItemStream<List<VariantContext>>{
	private static final Log logger = LogFactory.getLog(VariantListWriter.class);	
	private Resource resource = null;
	private VariantContextWriter vcfFileWriter = null;
	

	@Override
	public void open(final ExecutionContext arg0) throws ItemStreamException {
		try {
			final VariantContextWriterBuilder vcwb = new VariantContextWriterBuilder();
			vcwb.setOutputFile(Objects.requireNonNull(this.resource).getFile());
			this.vcfFileWriter =vcwb.build();
			}
		catch(IOException err) {
			throw new ItemStreamException(err);
			}
		
	}

	@Override
	public void close() throws ItemStreamException {
		if(this.vcfFileWriter!=null) this.vcfFileWriter.close();
	}

	@Override
	public void update(ExecutionContext arg0) throws ItemStreamException {
		logger.debug("update");
	}
	@Override
	public void write(List<? extends List<VariantContext>> L) throws Exception {

		}
	
	
	@Override
	public void setResource(final Resource rsrc) {
		this.resource = rsrc;
		
	}


}

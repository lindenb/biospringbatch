package com.github.lindenb.biospringbatch.bio;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.StepListener;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.core.io.Resource;

import htsjdk.samtools.util.CloseableIterator;
import htsjdk.samtools.util.CloserUtil;
import htsjdk.samtools.util.Interval;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;

public class VariantListReader 	implements 
	ItemStreamReader<List<VariantContext>>,
	ResourceAwareItemReaderItemStream<List<VariantContext>>,
	StepExecutionListener
	{
	private static final Log LOG = LogFactory.getLog(VariantListReader.class);	
	private Resource resource = null;
	private VCFFileReader vcfFileReader = null;
	private CloseableIterator<VariantContext> iter=null;
	private Interval interval = null;
	private StepExecution stepExecution = null;

	@Override
	public void beforeStep(final StepExecution stepExecution) {
		this.stepExecution = stepExecution;
		}
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return stepExecution.getExitStatus();
		}
	
	
	@Override
	public void open(final ExecutionContext ctx) throws ItemStreamException {
		try {
			this.vcfFileReader = new VCFFileReader(
					Objects.requireNonNull(this.getResource()).getFile(),
					getInterval()!=null
					);
			Objects.requireNonNull(this.stepExecution).
				getExecutionContext().
				put("header",this.vcfFileReader.getFileHeader());
			if(this.getInterval()==null)
				{
				this.iter = this.vcfFileReader.iterator();
				}
			else
				{
				final Interval seg = this.getInterval();
				this.iter = this.vcfFileReader.query(
					seg.getContig(),
					seg.getStart(),
					seg.getEnd()
					);
				}
			}
		catch(final IOException err) {
			LOG.warn("Cannot open", err);
			stop();
			throw new ItemStreamException(err);
			}
		
	}
	
	public void setInterval(final Interval interval) {
		this.interval = interval;
		}
	
	public Interval getInterval() {
		return interval;
		}

	private void stop() {
		CloserUtil.close(this.iter);
		CloserUtil.close(this.vcfFileReader);
		this.iter = null;
		this.vcfFileReader = null;
		}
	
	@Override
	public void close() throws ItemStreamException {
		stop();
	}

	@Override
	public void update(final ExecutionContext ctx) throws ItemStreamException {
		LOG.warn("################updating "+ctx);
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

	public Resource getResource() {
		return this.resource;
		}
}

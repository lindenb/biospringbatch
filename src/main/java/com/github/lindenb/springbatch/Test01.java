package com.github.lindenb.springbatch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import htsjdk.variant.variantcontext.VariantContext;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.batch.item.ItemProcessor;



import java.net.*;
public class Test01
	{
	public static class ReportFieldSetMapper implements FieldSetMapper<URL> {
	@Override
	public URL mapFieldSet(FieldSet fieldSet) throws BindException {

		try {
			return new URL("http://www.google.com/a/b/c");
			}
		catch(Exception err)
			{
			throw new BindException("bou","err");
			}

	}

}
	public static class CustomItemProcessor implements ItemProcessor<List<VariantContext>, List<VariantContext>>
		{
		@Override
		public List<VariantContext> process(List<VariantContext> L) throws Exception {

			System.err.println("Processing..." + L);
			return L;
			}
		}
	
        public static void main(String[] args) {

	String[] springConfig  =
		{
			"spring/batch/jobs/job-hello-world.xml"
		};

	ApplicationContext context =
			new ClassPathXmlApplicationContext(springConfig);

	JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
	Job job = (Job) context.getBean("helloWorldJob");

	try {

		JobExecution execution = jobLauncher.run(job, new JobParameters());
		System.out.println("Exit Status : " + execution.getStatus());

	} catch (Exception e) {
		e.printStackTrace();
	}

	System.out.println("Done");

  }
	}

package com.github.lindenb.biospringbatch.bio;

public interface FastaSequence {
public String getName();
public String getSequence();
public char charAt(int i);
public int length();
default public int getSize() { return this.length();}
public static FastaSequence from(final String name,final String seq) {
	return new FastaSequence() {
		@Override
		public int length() {return seq.length();}
		@Override
		public String getSequence() {return seq;}
		@Override
		public String getName() {return name;}
		@Override
		public char charAt(int i)  {return seq.charAt(i);}
		};
	}
}

package edu.nju.hellowworld.filters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class BufServletOutputStream extends ServletOutputStream {
	ByteArrayOutputStream bufferedOut;
	public BufServletOutputStream() {
		bufferedOut = new ByteArrayOutputStream();
	}
	public void write(int i) throws IOException {
		bufferedOut.write(i);
	}
	public byte[] toByteArray() {
		return bufferedOut.toByteArray();
	}
	public void reset() {
		bufferedOut.reset();
	}
	@Override
	public boolean isReady() {
		return false;
	}
	@Override
	public void setWriteListener(WriteListener arg0) {
		
	}
}

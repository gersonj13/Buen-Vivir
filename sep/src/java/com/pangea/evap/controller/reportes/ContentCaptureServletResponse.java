package com.pangea.evap.controller.reportes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ContentCaptureServletResponse extends HttpServletResponseWrapper {

    private ByteArrayOutputStream contentBuffer;
    private PrintWriter writer;

    public ContentCaptureServletResponse(HttpServletResponse originalResponse) {
        super(originalResponse);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer == null) {
            contentBuffer = new ByteArrayOutputStream();
            writer = new PrintWriter(contentBuffer);
        }
        return writer;
    }

    public String getContent() throws IOException {
        this.getWriter();
        writer.flush();
        String xhtmlContent = new String(contentBuffer.toByteArray());
        xhtmlContent = xhtmlContent.replaceAll("<thead>|</thead>|"
                + "<tbody>|</tbody>", "");
        return xhtmlContent;
    }
}

package net.i2p.router.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.util.Arrays;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import net.i2p.data.Base64;
 

 /**
  *
  * @author cacapo
  */

public class CodedIconRendererServlet extends HttpServlet {
 
    public static final long serialVersionUID = 16851750L;
    
    String base = net.i2p.I2PAppContext.getGlobalContext().getBaseDir().getAbsolutePath();
    String file = "docs" + java.io.File.separatorChar + "themes" + java.io.File.separatorChar + "console" +  java.io.File.separatorChar + "images" + java.io.File.separatorChar + "plugin.png";
       


     @Override

     protected void service(HttpServletRequest srq, HttpServletResponse srs) throws ServletException, IOException {
         byte[] data;
         String name = srq.getParameter("plugin");
         data  = NavHelper.getBinary(name);
         
         //set as many headers as are common to any outcome
         
         srs.setContentType("image/png");
         srs.setDateHeader("Expires", net.i2p.I2PAppContext.getGlobalContext().clock().now() + 86400000l);
         srs.setHeader("Cache-Control", "public, max-age=86400");
         OutputStream os = srs.getOutputStream();
         
         //Binary data is present
         if(data != null){
             srs.setHeader("Content-Length", Integer.toString(data.length));
             int content = Arrays.hashCode(data);
             int chksum = srq.getIntHeader("If-None-Match");//returns -1 if no such header
             //Don't render if icon already present
             if(content != chksum){
                 srs.setIntHeader("ETag", content);
                 try{
                     os.write(data);
                     os.flush();
                     os.close();
                 }catch(IOException e){
                     net.i2p.I2PAppContext.getGlobalContext().logManager().getLog(getClass()).warn("Error writing binary image data for plugin", e);
                 }
             } else {
                 srs.sendError(304, "Not Modified");
             }
         } else {
             //Binary data is not present but must be substituted by file on disk
             File pfile = new java.io.File(base, file);
             srs.setHeader("Content-Length", Long.toString(pfile.length()));
             try{
                 long lastmod = pfile.lastModified();
                 if(lastmod > 0){
                     long iflast = srq.getDateHeader("If-Modified-Since");
                     if(iflast >= ((lastmod/1000) * 1000)){
                         srs.sendError(304, "Not Modified");
                     } else {
                         srs.setDateHeader("Last-Modified", lastmod);
                         net.i2p.util.FileUtil.readFile(file, base, os); 
                     }
                     
                 }
             } catch(java.io.IOException e) {
                 if (!srs.isCommitted()) {
                     srs.sendError(403, e.toString());
                 } else {
                     net.i2p.I2PAppContext.getGlobalContext().logManager().getLog(getClass()).warn("Error serving plugin.png", e);
                     throw e;
                 }
             }
             
         }
     }
}

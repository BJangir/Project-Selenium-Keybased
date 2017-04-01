package com.automation.util;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

import com.sun.mail.imap.protocol.FLAGS;

public class MailUtil {
	
	static ConfigReader configReader=new ConfigReader();
	public static void main(String[] args) throws Exception {
		
		MailUtil mailUtil=new MailUtil();
		String getcontent = mailUtil.getcontent("donotreply@xceler.ninja");
		//String getcontent="<td role=\"module-content\"  valign=\"top\" height=\"100%\" style=\"padding: 0px 0px 0px 0px;\" bgcolor=\"#ffffff\"><div style=\"text-align: center;\">If this doesn&#39;t work, copy the&nbsp;link http://46.137.213.164:8109/#/verify/eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJWZXJpZnlUb2tlbjp4Y2VsZXIubmluamFAZ21haWwuY29tIiwiZW1haWwiOiJ4Y2VsZXIubmluamFAZ21haWwuY29tIiwiZXhwIjoxNDg5NTI0MDM2LCJpYXQiOjE0ODkyNjQ4MzYsImlzcyI6IlJlYWN0UGhvZW5peCIsImp0aSI6ImNhZjFlZjI4LTZkM2MtNDY1MS1hMmMzLWI5YmEzMTc3MjEyYiIsInBlbSI6e30sInN1YiI6IlZlcmlmeVRva2VuOnhjZWxlci5uaW5qYUBnbWFpbC5jb20iLCJ0eXAiOiJlbWFpbF92ZXJpZmljYXRpb24ifQ.2P-S_jjlyS7rSq3ib3bpscP8f0TObhtGTQ2iCYhFNr8MwZpUOV5Z2l3WF7tkVv7iSwFAM-rK__r-kn0mWK6npA in your browser&#39;s address bar.</div> </td>";
		/*String[] split = getcontent.split("\n");
		for(String s:split){
		  if(s.contains("copy the")&& s.contains("link")&& s.contains("in your browser")){
			  String substring = s.substring(s.indexOf("http"), s.indexOf("in your browser"));
			  System.out.println(substring);
		  }
		}
		*/
		System.out.println("My mail contet is "+getcontent);
		boolean deleteMail = mailUtil.deleteMail("donotreply@xceler.ninja");
		System.out.println("Deleted ---"+deleteMail);
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public String getcontent(String from) throws Exception{
		boolean isSenderExists=false;
		StringBuffer mailMessage=new StringBuffer();
		 Session session = Session.getDefaultInstance(configReader.getProperties(), null);
		 Store store = session.getStore("imaps");
		  store.connect("smtp.gmail.com", configReader.getKeyValue("test.user"), configReader.getKeyValue("test.password"));
		  Folder inbox = store.getFolder("inbox");
         inbox.open(Folder.READ_ONLY);
         int messageCount = inbox.getMessageCount();
         System.out.println("Total Messages:- " + messageCount);
         
         Message[] messages = inbox.getMessages();
         System.out.println("------------------------------");
         for(Message msg:messages){
       	  System.out.println("Mail Subject:- " + msg.getSubject());
       	  System.out.println("Content type" + msg.getContentType());
       	  System.out.println("Content address" + msg.getFrom()[0]);
       	  
       	  Address[] froms = msg.getFrom();
       	  String string = InternetAddress.toString(froms);
       	  if(string.contains(from)){
       		isSenderExists=true;
       		Object msgContent = msg.getContent();
         	  if(msgContent instanceof Multipart){
         		  Multipart multipart = (Multipart) msgContent;

         	         System.out.println("BodyPart, MultiPartCount: "+multipart.getCount());

         	         for (int j = 0; j < multipart.getCount(); j++) {

         	          BodyPart bodyPart = multipart.getBodyPart(j);

         	          String disposition = bodyPart.getDisposition();

         	          if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) { 
         	              System.out.println("Mail have some attachment");

         	              DataHandler handler = bodyPart.getDataHandler();
         	              System.out.println("file name : " + handler.getName());                                 
         	            }
         	          else { 
         	                 // content = getText(bodyPart);  // the changed code   
         	        	 // System.out.println("Body part:----"+bodyPart.getContent());
         	        	 mailMessage.append(bodyPart.getContent());
         	            }
         	        }
         	  }
         	  else{
         		 mailMessage.append(msgContent.toString());
         		 // System.out.println("Content is" + msgContent.toString()); 
         	  }
           
           }
           

       	  }
       	  
       	       
         inbox.close(true);
         store.close();
         
         if(!isSenderExists){
        	 System.out.println("No record  found from "+from);
         }
		return mailMessage.toString();
		
	}
	
	
	public boolean deleteMail(String from){
    boolean idelted = false;
	try{
	Session session = Session.getDefaultInstance(configReader.getProperties(), null);
	 Store store = session.getStore("imaps");
	  store.connect("smtp.gmail.com", configReader.getKeyValue("test.user"), configReader.getKeyValue("test.password"));
	  Folder inbox = store.getFolder("inbox");
   //  inbox.open(Folder.READ_ONLY);
	  inbox.open(Folder.READ_WRITE);
     int messageCount = inbox.getMessageCount();
     System.out.println("Total Messages:- " + messageCount);
     
     Message[] messages = inbox.getMessages();
     System.out.println("------------------------------");
     for(Message msg:messages){
   	  System.out.println("Mail Subject:- " + msg.getSubject());
   	  System.out.println("Content type" + msg.getContentType());
   	  System.out.println("Content address" + msg.getFrom()[0]);
   	  
   	  Address[] froms = msg.getFrom();
   	  String string = InternetAddress.toString(froms);
   	  System.out.println(string);
  if(string.contains(from))  
  {
	  msg.setFlag(FLAGS.Flag.DELETED, true);  
	  idelted=true;
	  
  }
      
     
     }
     
     if(!idelted){
    	 System.out.println("NO records found to delete");
    	 return false;
     }
}catch(Exception e){
	e.printStackTrace();
	return false;
}
	return idelted;
		           
	}
}

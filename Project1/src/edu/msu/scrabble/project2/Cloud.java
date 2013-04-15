package edu.msu.scrabble.project2;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.util.Base64;
import android.util.Log;
import android.util.Xml;

public class Cloud {
	
	private static final String NEW_USER_URL = "http://www.cse.msu.edu/~dearauj6/cse476/tinkertoys-new-user.php";
	private static final String LOGIN_URL = "http://www.cse.msu.edu/~dearauj6/cse476/tinkertoys-login.php";
	private static final String MAGIC = "NechAtHa6RuzeR8x";
	private static final String UTF8 = "UTF-8";
	
	
	public Cloud() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean loginToGame(String user, String pw) {
		final String userF = user.trim();
		final String passF = pw.trim();
		
		if(user.length() == 0 || pw.length() == 0){
			return false;
		}
		 // Create an XML packet with the information about the current image
        XmlSerializer xml = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        
        try {
            xml.setOutput(writer);
            
            xml.startDocument("UTF-8", true);
            
            xml.startTag(null, "tinker");
            xml.attribute(null, "user", userF);
            xml.attribute(null, "pw", passF);
            xml.attribute(null, "magic", MAGIC);

            
            xml.endTag(null, "tinker");
            
            xml.endDocument();

        } catch (IOException e) {
            // This won't occur when writing to a string
            return false;
        }
        
        final String xmlStr = writer.toString();
        /*
         * Convert the XML into HTTP POST data
         */
        String postDataStr;
        try {
            postDataStr = "xml=" + URLEncoder.encode(xmlStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        
        /*
         * Send the data to the server
         */
        byte[] postData = postDataStr.getBytes();
        InputStream stream = null;
        try {
            URL url = new URL(LOGIN_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
            conn.setUseCaches(false);

            OutputStream out = conn.getOutputStream();
            out.write(postData);
            out.close();

            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            } 
            stream = conn.getInputStream();
            //logStream(stream);
            
            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xmlR = Xml.newPullParser();
                xmlR.setInput(stream, UTF8);
                
                xmlR.nextTag();      // Advance to first tag
                xmlR.require(XmlPullParser.START_TAG, null, "tinker");
                
                String status = xmlR.getAttributeValue(null, "status");
                if(status.equals("no")) {
                    return false;
                }
                
                // We are done
            } catch(XmlPullParserException ex) {
                return false;
            } catch(IOException ex) {
                return false;
            }
            
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch(IOException ex) {
                    // Fail silently
                }
            }
        }
        return true;
		
	}
	
	public boolean newUser(String user, String pw, String pwC)
	{
		// Create an XML packet with the information about the current image
        XmlSerializer xml = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        
        try {
            xml.setOutput(writer);
            
            xml.startDocument("UTF-8", true);
            
            xml.startTag(null, "create_user");
            xml.attribute(null, "magic", MAGIC);
            xml.startTag(null, "new_user");
            xml.attribute(null, "user", user);
            xml.attribute(null, "pw", pw);
            xml.endTag(null, "new_user");
            
            xml.endTag(null, "create_user");
            
            xml.endDocument();

        } catch (IOException e) {
            // This won't occur when writing to a string
            return false;
        }
        
        final String xmlStr = writer.toString();
        
        /*
         * Convert the XML into HTTP POST data
         */
        String postDataStr;
        try {
            postDataStr = "xml=" + URLEncoder.encode(xmlStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        
        /*
         * Send the data to the server
         */
        byte[] postData = postDataStr.getBytes();
        
        InputStream stream = null;
        try {
            URL url = new URL(NEW_USER_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
            conn.setUseCaches(false);

            OutputStream out = conn.getOutputStream();
            out.write(postData);
            out.close();

            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            } 
            
            stream = conn.getInputStream();
            //logStream(stream);
            
            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xmlR = Xml.newPullParser();
                xmlR.setInput(stream, UTF8);
                
                xmlR.nextTag();
                // Advance to first tag
                xmlR.require(XmlPullParser.START_TAG, null, "create_user");
                
                String status = xmlR.getAttributeValue(null, "status");
                if(status.equals("no")) {
                    return false;
                }
                
                // We are done
            } catch(XmlPullParserException ex) {
                return false;
            } catch(IOException ex) {
                return false;
            }
            
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch(IOException ex) {
                    // Fail silently
                }
            }
        }
        return true;
	}
	
	public boolean writeUserInfo(String p1Name, int p1Score, String p1State, String p2Name,int p2Score, String p2State, String answer, String tip, String category) 
	{
		 // Create an XML packet with the information about the current image
        XmlSerializer xml = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        
        try {
            xml.setOutput(writer);
            
            xml.startDocument("UTF-8", true);
            
            xml.startTag(null, "tinker");
    
            xml.attribute(null, "magic", MAGIC);
            xml.attribute(null, "Player1", p1Name);
            xml.attribute(null, "P1state", p1State);
            xml.attribute(null, "P1score", String.valueOf(p1Score));
            xml.attribute(null, "Player2", p2Name);
            xml.attribute(null, "P2state", p2State);
            xml.attribute(null, "P2score", String.valueOf(p2Score));
            xml.attribute(null, "Tip", tip);
            xml.attribute(null, "Answer", answer);
            xml.attribute(null, "Category", category);

            
            xml.endTag(null, "tinker");
            
            xml.endDocument();

        } catch (IOException e) {
            // This won't occur when writing to a string
            return false;
        }
        
        final String xmlStr = writer.toString();
        /*
         * Convert the XML into HTTP POST data
         */
        String postDataStr;
        try {
            postDataStr = "xml=" + URLEncoder.encode(xmlStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        
        /*
         * Send the data to the server
         */
        byte[] postData = postDataStr.getBytes();
        InputStream stream = null;
        try {
            URL url = new URL(LOGIN_URL); //TODO change that to new 

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
            conn.setUseCaches(false);

            OutputStream out = conn.getOutputStream();
            out.write(postData);
            out.close();

            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            } 
            stream = conn.getInputStream();
            //logStream(stream);
            
            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xmlR = Xml.newPullParser();
                xmlR.setInput(stream, UTF8);
                
                xmlR.nextTag();      // Advance to first tag
                xmlR.require(XmlPullParser.START_TAG, null, "tinker");
                
                String status = xmlR.getAttributeValue(null, "status");
                if(status.equals("no")) {
                    return false;
                }
                
                // We are done
            } catch(XmlPullParserException ex) {
                return false;
            } catch(IOException ex) {
                return false;
            }
            
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch(IOException ex) {
                    // Fail silently
                }
            }
        }
        return true;
		
	}
	
	public InputStream readUserInfo(String user)
	{
		String query = LOGIN_URL+"?user=" + user + "&magic=" + MAGIC;
        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }
            
            InputStream stream = conn.getInputStream();
            return stream;

        } catch (MalformedURLException e) {
            // Should never happen
            return null;
        } catch (IOException ex) {
            return null;
        }
	}
	
	public boolean saveDrawing(Picture pic, String user)
	{
		byte[] bytes = convertPicToBytes(pic);
		String byteString = Base64.encodeToString(bytes, Base64.URL_SAFE);
		
		// Create an XML packet with the information about the current image
        XmlSerializer xml = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        
        try {
            xml.setOutput(writer);
            
            xml.startDocument("UTF-8", true);
            
            xml.startTag(null, "tinker");
    
            xml.attribute(null, "magic", MAGIC);
            xml.attribute(null, "player", user);
            xml.attribute(null, "drawing", byteString);

            
            xml.endTag(null, "tinker");
            
            xml.endDocument();

        } catch (IOException e) {
            // This won't occur when writing to a string
            return false;
        }
        
        final String xmlStr = writer.toString();
        /*
         * Convert the XML into HTTP POST data
         */
        String postDataStr;
        try {
            postDataStr = "xml=" + URLEncoder.encode(xmlStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        
        /*
         * Send the data to the server
         */
        byte[] postData = postDataStr.getBytes();
        InputStream stream = null;
        try {
            URL url = new URL(LOGIN_URL); //TODO change that to new 

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
            conn.setUseCaches(false);

            OutputStream out = conn.getOutputStream();
            out.write(postData);
            out.close();

            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            } 
            stream = conn.getInputStream();
            //logStream(stream);
            
            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xmlR = Xml.newPullParser();
                xmlR.setInput(stream, UTF8);
                
                xmlR.nextTag();      // Advance to first tag
                xmlR.require(XmlPullParser.START_TAG, null, "tinker");
                
                String status = xmlR.getAttributeValue(null, "status");
                if(status.equals("no")) {
                    return false;
                }
                
                // We are done
            } catch(XmlPullParserException ex) {
                return false;
            } catch(IOException ex) {
                return false;
            }
            
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch(IOException ex) {
                    // Fail silently
                }
            }
        }
        return true;
		
		
	}
	
	public InputStream pullDrawing(String user)
	{
		String query = LOGIN_URL+"?user=" + user + "&magic=" + MAGIC;
        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }
            
            InputStream stream = conn.getInputStream();
            return stream;

        } catch (MalformedURLException e) {
            // Should never happen
            return null;
        } catch (IOException ex) {
            return null;
        }
	}
	
	private byte[] convertPicToBytes(Object Serialize)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectOutput output = null;
		try
		{
			output = new ObjectOutputStream(stream);   
			output.writeObject(Serialize);
			byte[] result = stream.toByteArray();
			return result;   
	    	  
		}
		catch(IOException e)
		{
			return null;
		}
		finally
		{
			try
			{
				stream.close();
				output.close();
			}
			catch(IOException e)
			{
				return null;
			}
		}
		
	}
	
	public Object convertBytesToPic(byte[] bytes)
	{
		ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
		ObjectInput input = null;
		try 
		{
    	  input = new ObjectInputStream(stream);
    	  Object obj = input.readObject();
    	  return obj;
    	}
		catch(IOException e)
    	{
    		return null;
    	}
		catch(ClassNotFoundException ce)
    	{
    		return null;
    	}
    	finally
    	{
    		try
    		{
	    	  stream.close();
	    	  input.close();
    		}
    		catch(IOException ioe)
    		{
        		return null;
        	}
    	}
	}
	
	public static void logStream(InputStream stream) {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));
    
        Log.e("476", "logStream: If you leave this in, code after will not work!");
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                Log.e("476", line);
            }
        } catch (IOException ex) {
            return;
        }
    }
	
	/**
     * Skip the XML parser to the end tag for whatever 
     * tag we are currently within.
     * @param xml the parser
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static void skipToEndTag(XmlPullParser xml) 
            throws IOException, XmlPullParserException {
        int tag;
        do
        {
            tag = xml.next();
            if(tag == XmlPullParser.START_TAG) {
                // Recurse over any start tag
                skipToEndTag(xml);
            }
        } while(tag != XmlPullParser.END_TAG && 
        tag != XmlPullParser.END_DOCUMENT);
    }

}

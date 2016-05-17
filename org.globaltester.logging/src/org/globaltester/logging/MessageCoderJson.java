package org.globaltester.logging;

import org.globaltester.lib.xstream.XstreamFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.CompositeClassLoader;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;



public class MessageCoderJson implements MessageEncoder, MessageDecoder {
	
	private XStream xstream;
	
	public MessageCoderJson() {
		HierarchicalStreamDriver hsd = new JettisonMappedXmlDriver();
		
		xstream = XstreamFactory.get(hsd);
		
		((CompositeClassLoader) xstream.getClassLoader()).add(XstreamFactory.class.getClassLoader());
	}
	
	@Override
	public String encode(Message messageObject) {
		return xstream.toXML(messageObject);
	}

	@Override
	public Message decode(String messageRep) {
		return (Message) xstream.fromXML(messageRep);
	}

}

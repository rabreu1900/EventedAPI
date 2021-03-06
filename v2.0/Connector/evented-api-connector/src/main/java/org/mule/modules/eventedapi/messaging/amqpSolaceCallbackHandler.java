package org.mule.modules.eventedapi.messaging;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.mule.modules.eventedapi.util.GenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class amqpSolaceCallbackHandler implements MessageListener, ICallback
{
	private static Logger logger = LoggerFactory.getLogger(amqpSolaceCallbackHandler.class);
	
	private ISubject _subject;
	
	public  amqpSolaceCallbackHandler()
	{
		logger.info("AMQP Solace Callback Handler init: !");
	}
	
	@Override
	public void onMessage(Message arg0) {
		// TODO Auto-generated method stub
		String _outMessage=null;
		
		try
		{
		
			if(arg0 instanceof TextMessage || arg0 instanceof ObjectMessage)
			{
				if (arg0 instanceof TextMessage)
				{
					TextMessage _message = (TextMessage) arg0;
					_outMessage = _message.getText();
				}
				if(arg0 instanceof ObjectMessage)
				{
					ObjectMessage _message = (ObjectMessage) arg0;
					_outMessage = (String) _message.getObject();
					
				}
				
				logger.info("Got Incomming AMQP Solace Message: "+ _outMessage);
				this.handleMessage(_outMessage);
			}
			
			
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
		}
		
	}

	@Override
	public void setSubject(ISubject pSubject) {
		// TODO Auto-generated method stub
		
		_subject=pSubject;
		
	}

	@Override
	public void handleMessage(Object pMessage) throws Exception {
		// TODO Auto-generated method stub

		Event _newEvent = new Event();
		_newEvent.setMessagePayload(pMessage);
		_newEvent.setEventId(GenUtil.getInstance().genEventUUID());
		_newEvent.setTransportType("amqpSolace");
		_subject.addInboundEvent(_newEvent);
		
	}

}

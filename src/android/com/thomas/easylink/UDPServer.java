package com.thomas.easylink;

import android.util.Log;
import org.apache.cordova.CallbackContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer implements Runnable {

	private static final int PORT = 20001;

	private byte[] msg = new byte[1024];

	private boolean life = true;

	private CallbackContext callbackContext;
	public UDPServer() {
	}

	/**
	 * @return the life
	 */
	public boolean isLife() {
		return life;
	}

	/**
	 * @param life
	 *            the life to set
	 */
	public void setLife(boolean life) {
		this.life = life;
	}
	public void setCallbackContext(CallbackContext callbackContext){
		this.callbackContext = callbackContext;
	}

	@Override
	public void run() {
		DatagramSocket dSocket = null;
		DatagramPacket dPacket = new DatagramPacket(msg, msg.length);
		try {
			dSocket = new DatagramSocket(PORT);
			while (life) {
				try {
					dSocket.receive(dPacket);
					int length = dPacket.getLength();
					byte[] receiveBytes = new byte[length];
					System.arraycopy(dPacket.getData(),dPacket.getOffset(),receiveBytes,0,length);
					String jsonStr = null;
					try {
						jsonStr = parseReceiveBytesToJsonStr(receiveBytes);
					} catch (JSONException e) {
						e.printStackTrace();
					}
//					String received = new String(dPacket.getData(),dPacket.getOffset(),dPacket.getLength());
					Log.i("msg sever received",jsonStr );
					if (callbackContext!=null){
						callbackContext.success(jsonStr);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			dSocket.close();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	private String parseReceiveBytesToJsonStr(byte[] receiveBytes) throws JSONException {
		JSONObject jsonObject  = new JSONObject();
		byte[] psnBytes = new byte[8];
		System.arraycopy(receiveBytes,0,psnBytes,0,8);
		Long psn = byteToLong(psnBytes);
		String ip = new String(receiveBytes,8,(receiveBytes.length-8));
		jsonObject.put("psn",psn);
		jsonObject.put("ip",ip.trim());
		return jsonObject.toString();
	}

	//byte数组转成long
	private long byteToLong(byte[] b) {
		long s = 0;
		long s0 = b[0] & 0xff;// 最低位
		long s1 = b[1] & 0xff;
		long s2 = b[2] & 0xff;
		long s3 = b[3] & 0xff;
		long s4 = b[4] & 0xff;// 最低位
		long s5 = b[5] & 0xff;
		long s6 = b[6] & 0xff;
		long s7 = b[7] & 0xff;

		// s0不变
		s1 <<= 8;
		s2 <<= 16;
		s3 <<= 24;
		s4 <<= 8 * 4;
		s5 <<= 8 * 5;
		s6 <<= 8 * 6;
		s7 <<= 8 * 7;
		s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
		return s;
	}
}

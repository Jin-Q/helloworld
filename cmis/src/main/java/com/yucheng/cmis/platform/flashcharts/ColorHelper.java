package com.yucheng.cmis.platform.flashcharts;

public class ColorHelper {
	
	   public static String getColor(){
		    StringBuffer stb = new StringBuffer();
			for(int i=0;i<6;i++){
				String s = (Math.random()*16)+"";
				String indexStr = s.substring(0,s.indexOf("."));
				
				if(indexStr.equals("10")){
					stb.append("A");
				}else
				if(indexStr.equals("11")){
					stb.append("B");
				}else
				if(indexStr.equals("12")){
					stb.append("C");
				}else
				if(indexStr.equals("13")){
					stb.append("D");
				}else
				if(indexStr.equals("14")){
					stb.append("E");
				}else
				if(indexStr.equals("15")){
					stb.append("F");
				}else{
					stb.append(indexStr);
				}
			}
			
//			System.out.println(stb.toString());
			return stb.toString();
	   }
	   
	   public static void main(String args[])
	   {
		      ColorHelper.getColor();   
	   }
}

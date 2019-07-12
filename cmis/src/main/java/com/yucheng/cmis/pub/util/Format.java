package com.yucheng.cmis.pub.util;


import java.util.ArrayList;

import com.yucheng.cmis.pub.MD5;

public class Format
{

    public Format(String s)
    {
        width = 0;
        precision = -1;
        pre = "";
        post = "";
        leadingZeroes = false;
        showPlus = false;
        alternate = false;
        showSpace = false;
        leftAlign = false;
        countSignInLen = true;
        fmt = ' ';
        int state = 0;
        int length = s.length();
        int parseState = 0;
        int i;
        for(i = 0; parseState == 0; i++)
            if(i >= length)
                parseState = 5;
            else
            if(s.charAt(i) == '%')
            {
                if(i < length - 1)
                {
                    if(s.charAt(i + 1) == '%')
                    {
                        pre = pre + '%';
                        i++;
                    } else
                    {
                        parseState = 1;
                    }
                } else
                {
                    throw new IllegalArgumentException();
                }
            } else
            {
                pre = pre + s.charAt(i);
            }

        while(parseState == 1) 
        {
            if(i >= length)
                parseState = 5;
            else
            if(s.charAt(i) == ' ')
                showSpace = true;
            else
            if(s.charAt(i) == '-')
                leftAlign = true;
            else
            if(s.charAt(i) == '+')
                showPlus = true;
            else
            if(s.charAt(i) == '0')
                leadingZeroes = true;
            else
            if(s.charAt(i) == '#')
                alternate = true;
            else
            if(s.charAt(i) == '~')
            {
                countSignInLen = false;
            } else
            {
                parseState = 2;
                i--;
            }
            i++;
        }
        while(parseState == 2) 
        {
            if(i >= length)
                parseState = 5;
            else
            if('0' <= s.charAt(i) && s.charAt(i) <= '9')
            {
                width = (width * 10 + s.charAt(i)) - 48;
                i++;
            } else
            if(s.charAt(i) == '.')
            {
                parseState = 3;
                precision = 0;
                i++;
            } else
            {
                parseState = 4;
            }
        }
        while(parseState == 3) 
        {
            if(i >= length)
                parseState = 5;
            else
            if('0' <= s.charAt(i) && s.charAt(i) <= '9')
            {
                precision = (precision * 10 + s.charAt(i)) - 48;
                i++;
            } else
            {
                parseState = 4;
            }
        }
        if(parseState == 4)
        {
            if(i >= length)
                parseState = 5;
            else
                fmt = s.charAt(i);
            i++;
        }
        if(i < length)
            post = s.substring(i, length);
    }

    public static double atof(String s)
    {
        int i = 0;
        int sign = 1;
        double r = 0.0D;
        double f = 0.0D;
        double p = 1.0D;
        int state = 0;
        for(; i < s.length() && Character.isWhitespace(s.charAt(i)); i++);
        if(i < s.length() && s.charAt(i) == '-')
        {
            sign = -1;
            i++;
        } else
        if(i < s.length() && s.charAt(i) == '+')
            i++;
        for(; i < s.length(); i++)
        {
            char ch = s.charAt(i);
            if('0' <= ch && ch <= '9')
            {
                if(state == 0)
                    r = (r * 10D + (double)ch) - 48D;
                else
                if(state == 1)
                {
                    p /= 10D;
                    r += p * (double)(ch - 48);
                }
            } else
            if(ch == '.')
            {
                if(state == 0)
                    state = 1;
                else
                    return (double)sign * r;
            } else
            if(ch == 'e' || ch == 'E')
            {
                long e = (int)parseLong(s.substring(i + 1), 10);
                return (double)sign * r * Math.pow(10D, e);
            } else
            {
                return (double)sign * r;
            }
        }

        return (double)sign * r;
    }

    public static int atoi(String s)
    {
        return (int)atol(s);
    }

    public static long atol(String s)
    {
        int i;
        for(i = 0; i < s.length() && Character.isWhitespace(s.charAt(i)); i++);
        if(i < s.length() && s.charAt(i) == '0')
        {
            if(i + 1 < s.length() && (s.charAt(i + 1) == 'x' || s.charAt(i + 1) == 'X'))
                return parseLong(s.substring(i + 2), 16);
            else
                return parseLong(s, 8);
        } else
        {
            return parseLong(s, 10);
        }
    }

    public static String convert(long x, int n, String d)
    {
        if(x == 0L)
            return "0";
        String r = "";
        int m = 1 << n;
        m--;
        for(; x != 0L; x >>>= n)
            r = d.charAt((int)(x & (long)m)) + r;

        return r;
    }

    private String expFormat(double d)
    {
        String f = "";
        int e = 0;
        double dd = d;
        double factor = 1.0D;
        if(d != 0.0D)
        {
            for(; dd > 10D; dd /= 10D)
            {
                e++;
                factor /= 10D;
            }

            for(; dd < 1.0D; dd *= 10D)
            {
                e--;
                factor *= 10D;
            }

        }
        if((fmt == 'g' || fmt == 'G') && e >= -4 && e < precision)
            return fixedFormat(d);
        d *= factor;
        f = f + fixedFormat(d);
        if(fmt == 'e' || fmt == 'g')
            f = f + "e";
        else
            f = f + "E";
        String p = "000";
        if(e >= 0)
        {
            f = f + "+";
            p = p + e;
        } else
        {
            f = f + "-";
            p = p + -e;
        }
        return f + p.substring(p.length() - 3, p.length());
    }

    private String fixedFormat(double d)
    {
        boolean removeTrailing = (fmt == 'G' || fmt == 'g') && !alternate;
        if(d > 9.2233720368547758E+018D)
            return expFormat(d);
        if(precision == 0)
            return (long)d + (removeTrailing ? "" : ".");
        long whole = (long)d;
        double fr = d - (double)whole;
        if(fr >= 1.0D || fr < 0.0D)
            return expFormat(d);
        double factor = 1.0D;
        String leadingZeroes = "";
        for(int i = 1; i <= precision && factor <= 9.2233720368547758E+018D; i++)
        {
            factor *= 10D;
            leadingZeroes = leadingZeroes + "0";
        }

        long l = (long)(factor * fr);
        if((double)l >= factor)
        {
            l = 0L;
            whole++;
        }
        String z = leadingZeroes + l;
        z = "." + z.substring(z.length() - precision, z.length());
        if(removeTrailing)
        {
            int t;
            for(t = z.length() - 1; t >= 0 && z.charAt(t) == '0'; t--);
            if(t >= 0 && z.charAt(t) == '.')
                t--;
            z = z.substring(0, t + 1);
        }
        return whole + z;
    }

    public String form(String s)
    {
        if(fmt != 's')
            throw new IllegalArgumentException();
        if(precision >= 0 && precision < s.length())
            s = s.substring(0, precision);
        return pad(s);
    }

    public String form(Integer x)
    {
        return form(x.intValue());
    }

    public String form(int x)
    {
        int s = 0;
        String r;
        if(fmt == 'd' || fmt == 'i')
        {
            if(x < 0)
            {
                r = ("" + x).substring(1);
                s = -1;
            } else
            {
                r = "" + x;
                s = 1;
            }
        } else
        {
            long xl = (long)x & 4294967295L;
            if(fmt == 'u')
            {
                r = "" + xl;
                s = 1;
            } else
            if(fmt == 'o')
                r = convert(xl, 3, "01234567");
            else
            if(fmt == 'x')
                r = convert(xl, 4, "0123456789abcdef");
            else
            if(fmt == 'X')
                r = convert(xl, 4, "0123456789ABCDEF");
            else
                throw new IllegalArgumentException();
        }
        return pad(sign(s, r));
    }

    public String form(Long x)
    {
        return form(x.longValue());
    }

    public String form(long x)
    {
        int s = 0;
        String r;
        if(fmt == 'd')
        {
            if(x < 0L)
            {
                r = ("" + x).substring(1);
                s = -1;
            } else
            {
                r = "" + x;
                s = 1;
            }
        } else
        if(fmt == 'i')
        {
            int xx = (int)x;
            if(xx < 0)
            {
                r = ("" + xx).substring(1);
                s = -1;
            } else
            {
                r = "" + xx;
                s = 1;
            }
        } else
        if(fmt == 'u')
        {
            long xl = x & 4294967295L;
            r = "" + xl;
            s = 1;
        } else
        if(fmt == 'o')
            r = convert(x, 3, "01234567");
        else
        if(fmt == 'x')
            r = convert(x, 4, "0123456789abcdef");
        else
        if(fmt == 'X')
            r = convert(x, 4, "0123456789ABCDEF");
        else
            throw new IllegalArgumentException();
        return pad(sign(s, r));
    }

    public String form(Float x)
    {
        return form(x.doubleValue());
    }

    public String form(Double x)
    {
        return form(x.doubleValue());
    }

    public String form(double x)
    {
        if(precision < 0)
            precision = 6;
        int s = 1;
        if(x < 0.0D)
        {
            x = -x;
            s = -1;
        }
        String r;
        if(fmt == 'f')
            r = fixedFormat(x);
        else
        if(fmt == 'e' || fmt == 'E' || fmt == 'g' || fmt == 'G')
            r = expFormat(x);
        else
            throw new IllegalArgumentException();
        return pad(sign(s, r));
    }

    public String form(Character c)
    {
        return form(c.charValue());
    }

    public String form(char c)
    {
        if(fmt != 'c')
        {
            throw new IllegalArgumentException();
        } else
        {
            String r = "" + c;
            return pad(r);
        }
    }

    private String pad(String r)
    {
        String p = repeat(' ', width - r.length());
        if(leftAlign)
            return pre + r + p + post;
        else
            return pre + p + r + post;
    }

    private static long parseLong(String s, int base)
    {
        int i = 0;
        int sign = 1;
        long r = 0L;
        for(; i < s.length() && Character.isWhitespace(s.charAt(i)); i++);
        if(i < s.length() && s.charAt(i) == '-')
        {
            sign = -1;
            i++;
        } else
        if(i < s.length() && s.charAt(i) == '+')
            i++;
        for(; i < s.length(); i++)
        {
            char ch = s.charAt(i);
            if('0' <= ch && ch < 48 + base)
                r = (r * (long)base + (long)ch) - 48L;
            else
            if('A' <= ch && ch < (65 + base) - 10)
                r = ((r * (long)base + (long)ch) - 65L) + 10L;
            else
            if('a' <= ch && ch < (97 + base) - 10)
                r = ((r * (long)base + (long)ch) - 97L) + 10L;
            else
                return r * (long)sign;
        }

        return r * (long)sign;
    }

    private static String repeat(char c, int n)
    {
        if(n <= 0)
            return "";
        StringBuffer s = new StringBuffer(n);
        for(int i = 0; i < n; i++)
            s.append(c);

        return s.toString();
    }

    private String sign(int s, String r)
    {
        String p = "";
        if(s < 0)
            p = "-";
        else
        if(s > 0)
        {
            if(showPlus)
                p = "+";
            else
            if(showSpace)
                p = " ";
        } else
        {
            if(fmt == 'o' && alternate && r.length() > 0 && r.charAt(0) != '0')
                p = "0";
            else
            if(fmt == 'x' && alternate)
                p = "0x";
            else
            if(fmt == 'X' && alternate)
                p = "0X";
        }
        int w = 0;
        if(leadingZeroes)
            w = width;
        else
        if((fmt == 'u' || fmt == 'd' || fmt == 'i' || fmt == 'x' || fmt == 'X' || fmt == 'o') && precision > 0)
            w = precision;
        if(countSignInLen)
            return p + repeat('0', w - p.length() - r.length()) + r;
        else
            return p + repeat('0', w - r.length()) + r;
    }

    private static String[] split(String s)
    {
        ArrayList list = new ArrayList();
        int lasti = 0;
        int i = s.indexOf("%");
        if(i == -1)
            return (new String[] {
                s
            });
        if(i > 0)
        {
            list.add("+" + s.substring(0, i));
            lasti = i;
            i++;
            i = s.indexOf("%", i);
        } else
        if(i == 0)
            i = s.indexOf("%", i + 1);
        for(; i != -1; i = s.indexOf("%", i))
        {
            String ss = s.substring(lasti, i);
            if(ss.equals("%"))
            {
                lasti = i;
                i++;
                i = s.indexOf("%", i);
                if(i != -1)
                    ss = s.substring(lasti, i);
                else
                    ss = s.substring(lasti);
                list.add("+" + ss);
            } else
            {
                list.add(" " + ss);
            }
            if(i == -1)
            {
                lasti = i;
                break;
            }
            lasti = i;
            i++;
        }

        if(lasti != -1)
            list.add(" " + s.substring(lasti));
        String ret[] = new String[list.size()];
        for(i = 0; i < list.size(); i++)
            ret[i] = (String)list.get(i);

        return ret;
    }

    public static String sprintf(String s, int params[])
    {
        if(s == null || params == null)
            return s;
        StringBuffer result = new StringBuffer("");
        String ss[] = split(s);
        int p = 0;
        for(int i = 0; i < ss.length; i++)
        {
            char c = ss[i].charAt(0);
            String t = ss[i].substring(1);
            if(c == '+')
            {
                result.append(t);
            } else
            {
                int param = params[p];
                result.append((new Format(t)).form(param));
                p++;
            }
        }

        return result.toString();
    }

    public static String sprintf(String s, Character params[])
    {
        return sprintf(s, (Object[])params);
    }

    public static String sprintf(String s, Float params[])
    {
        return sprintf(s, (Object[])params);
    }

    public static String sprintf(String s, Double params[])
    {
        return sprintf(s, (Object[])params);
    }

    public static String sprintf(String s, Long params[])
    {
        return sprintf(s, (Object[])params);
    }

    public static String sprintf(String s, Integer params[])
    {
        return sprintf(s, (Object[])params);
    }

    public static String sprintf(String s, String params[])
    {
        return sprintf(s, (Object[])params);
    }

    public static String sprintf(String s, Object params[])
    {
        if(s == null || params == null)
            return s;
        StringBuffer result = new StringBuffer("");
        String ss[] = split(s);
        int p = 0;
        for(int i = 0; i < ss.length; i++)
        {
            char c = ss[i].charAt(0);
            String t = ss[i].substring(1);
            if(c == '+')
            {
                result.append(t);
            } else
            {
                Object param = params[p];
                if(param instanceof Integer)
                    result.append((new Format(t)).form((Integer)param));
                else
                if(param instanceof Long)
                    result.append((new Format(t)).form((Long)param));
                else
                if(param instanceof Character)
                    result.append((new Format(t)).form((Character)param));
                else
                if(param instanceof Double)
                    result.append((new Format(t)).form((Double)param));
                else
                if(param instanceof Double)
                    result.append((new Format(t)).form((Float)param));
                else
                    result.append((new Format(t)).form(param.toString()));
                p++;
            }
        }

        return result.toString();
    }

    public static String sprintf(String fmt, String x)
    {
        return (new Format(fmt)).form(x);
    }

    public static String sprintf(String fmt, Integer x)
    {
        return (new Format(fmt)).form(x);
    }

    public static String sprintf(String fmt, int x)
    {
        return (new Format(fmt)).form(x);
    }

    public static String sprintf(String fmt, Long x)
    {
        return (new Format(fmt)).form(x);
    }

    public static String sprintf(String fmt, long x)
    {
        return (new Format(fmt)).form(x);
    }

    public static String sprintf(String fmt, Float x)
    {
        return (new Format(fmt)).form(x);
    }

    public static String sprintf(String fmt, Double x)
    {
        return (new Format(fmt)).form(x);
    }

    public static String sprintf(String fmt, double x)
    {
        return (new Format(fmt)).form(x);
    }

    public static String sprintf(String fmt, Character x)
    {
        return (new Format(fmt)).form(x);
    }

    public static String sprintf(String fmt, char x)
    {
        return (new Format(fmt)).form(x);
    }

    private final static String chars64 =
	      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz012345678909";
	  public final static String generate() {
		    long id1 = System.currentTimeMillis() & 0x3FFFFFFFL;
		    long id3 = randomLong( -0x80000000L, 0x80000000L) & 0x3FFFFFFFL;

		    String str = Format.convert(id1, 6, chars64) +
		        Format.convert(id3, 6, chars64);
		    //return locate + time + "_" + md5.getMD5ofStr(str);
		      MD5 md5 = new MD5();
		      return  md5.getMD5ofStr(str);
		  }
	  public static long randomLong(long min, long max) {
		    return min + (long) (Math.random() * (max - min));
		  } 
    
    private boolean alternate;
    private boolean countSignInLen;
    private char fmt;
    private boolean leadingZeroes;
    private boolean leftAlign;
    private String post;
    private String pre;
    private int precision;
    private boolean showPlus;
    private boolean showSpace;
    private int width;
    
    public static void main(String[] a){
    	int i=0;
    	while(i<100){
    		System.err.println(Format.generate());
    		i++;
    	}
    	
    }
}


 
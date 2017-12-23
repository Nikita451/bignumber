package nikita;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 *
 * @author nikita
 */
public class BigNumber {

    private ArrayList<Byte> number = new ArrayList<Byte>(); 
    
    public BigNumber(String n) {
        makeNumber(n);
    }
    
    public BigNumber(ArrayList<Byte> n)
    {
        number = (ArrayList<Byte>) n.clone();
    }
    
    private void makeNumber(String s)
    {
        char[] mas = s.toCharArray();
        final char zero = '0';

        for (int i=mas.length-1;i>=0;i--)
        {
            byte buffer = (byte) ((byte)mas[i] - (byte)zero);
            this.number.add(buffer);
        }
    }
    
   

    @Override
    public String toString() {
        String res = "";
        int mySize = this.number.size();
        
        for (int i=mySize-1;i>=0;i--)
        {
            res+=String.valueOf(this.number.get(i));
        }
        return res;
    }
    
    public BigNumber plus(BigNumber secondNumber)
    {
        byte buffer = 0;
        int minLength = 0;
        int maxLength = 0;
        ArrayList<Byte> newList = new ArrayList<Byte>();
        
        if (this.number.size()<secondNumber.number.size())
        {
            minLength = this.number.size();
            maxLength = secondNumber.number.size();
        }
        else
        {
            minLength =secondNumber.number.size();
            maxLength = this.number.size();
        }
        
        int index = 0;
        byte ost = 0;
        byte newn = 0;
        while (index<minLength)
        {
            byte sum = (byte) (secondNumber.number.get(index) + this.number.get(index) + ost);
            ost = (byte) (sum / 10);
            newn = (byte) (sum % 10);
            newList.add(newn);
            index++;
        }
        
        if (maxLength==minLength)
        {
            if (ost>0)
            newList.add(ost);        
            return new BigNumber(newList);
        }
        else 
        {
            if (maxLength==this.number.size())
            {
                while (index<maxLength)
                {
                    byte sum = (byte) (this.number.get(index) + ost);
                    ost = (byte) (sum / 10);
                    newn = (byte) (sum % 10);
                    newList.add(newn);
                    index++;
                }
            }
            else
            {
                while (index<maxLength)
                {
                     byte sum = (byte) (secondNumber.number.get(index) + ost);
                     ost = (byte) (sum / 10);
                     newn = (byte) (sum % 10);
                     newList.add(newn);
                     index++;
                }
            }
        }
        
        if (ost>0)
        {
            newList.add(ost);
        }
        
        return new BigNumber(newList);
    }
    
    private BigNumber multiplyFromOneToTen(byte secNumber) 
    {
        int index = 0;
        int mySize = this.number.size();
        ArrayList<Byte> newList = new ArrayList<Byte>();
        byte ost = 0;
        byte newn = 0;
        while (index<mySize)
        {
            byte mul= (byte) (this.number.get(index)*secNumber + ost) ;
            ost = (byte) (mul / 10);
            newn = (byte) (mul % 10);
            newList.add(newn);
            index++;
        }
        if (ost>0)
        {
            newList.add(ost);
        }
        return new BigNumber(newList);
    }
    
    public void multyplayTen(int col)
    {
        for (int t=0;t<col;t++)
        {
            this.number.add(0, (byte)0);
        }
    }
    
    public BigNumber multiply(BigNumber secN)
    {
        int mySize = secN.number.size();
        BigNumber resSum = this.multiplyFromOneToTen(secN.number.get(0));
        for (int i=1;i<mySize;i++)
        {
           BigNumber buffer = this.multiplyFromOneToTen(secN.number.get(i));
           buffer.multyplayTen(i);
           resSum = resSum.plus(buffer);
        }
        
        return resSum;
    }
    
}

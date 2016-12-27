package me.rebi.simplechartlibrary;

/**
 * 作者: Warm.
 * 日期: 2016/12/17 10:06.
 * 联系: QQ-865741452.
 * 内容:
 */
public class Value {
    private int color;
    private double value;
    private String str;

    public Value(String str, double value, int color) {
        this.str = str;
        this.value = value;
        this.color = color;
    }


    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

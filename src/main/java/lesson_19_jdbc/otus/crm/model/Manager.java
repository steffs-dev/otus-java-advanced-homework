package lesson_19_jdbc.otus.crm.model;

import lesson_19_jdbc.otus.Id;

public class Manager {
    @Id
    private Long no;
    private String label;
    private String param1;

    public Manager() {
    }

    public Manager(String label) {
        this.label = label;
    }

    public Manager(Long no, String label) {
        this(label);
        this.no = no;
    }

    public Manager(Long no, String label, String param1) {
        this(no, label);
        this.param1 = param1;
    }

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    @Override
    public String toString() {
        return "Manager{" + "no=" + no + ", label='" + label + '\'' + ", param1='" + param1 + '\'' + '}';
    }
}

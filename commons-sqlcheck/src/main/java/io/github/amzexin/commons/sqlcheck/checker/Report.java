package io.github.amzexin.commons.sqlcheck.checker;

/**
 * 检查报告
 */
public class Report {
    /**
     * 通过标识
     */
    private boolean pass;
    /**
     * 错误提示
     */
    private String desc;

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static Report pass() {
        Report report = new Report();
        report.setPass(true);
        report.setDesc("通过所有检查规则");
        return report;
    }

    public static Report unPass(String reason) {
        Report report = new Report();
        report.setPass(false);
        report.setDesc(reason);
        return report;
    }

    @Override
    public String toString() {
        return "Report{" +
                "pass=" + pass +
                ", desc='" + desc + '\'' +
                '}';
    }
}

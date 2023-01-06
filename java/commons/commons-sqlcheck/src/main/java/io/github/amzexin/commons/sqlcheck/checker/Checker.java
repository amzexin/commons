package io.github.amzexin.commons.sqlcheck.checker;

import io.github.amzexin.commons.sqlcheck.SQLCheckerException;
import io.github.amzexin.commons.sqlcheck.statement.StatementTypeEnum;
import net.sf.jsqlparser.statement.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 规则检查器
 */
public class Checker {
    /**
     * 规则集
     */
    private Map<StatementTypeEnum, List<CheckRule>> rules = new ConcurrentHashMap<>();

    /**
     * 注册规则
     *
     * @param rule
     */
    public void registeRule(CheckRule rule) {
        this.rules
                .computeIfAbsent(rule.statementType(), statementTypeEnum -> new ArrayList<>())
                .add(rule);
    }

    /**
     * 检查SQl语句
     *
     * @param statement SQL语句
     * @return 规则检查报告
     */
    public Report check(Statement statement) throws SQLCheckerException {

        StatementTypeEnum statementType = StatementTypeEnum.getByStatement(statement);
        if (statementType == null) {
            throw new SQLCheckerException("目前不支持该SQL语句类型的审核");
        }

        List<CheckRule> checkRules = rules.get(statementType);
        if (checkRules == null || checkRules.isEmpty()) {
            throw new SQLCheckerException("检查器中缺少该SQL语句类型的检查规则");
        }

        for (CheckRule rule : checkRules) {
            Report report = rule.match(statement);
            if (!report.isPass()) {
                return report;
            }
        }
        return Report.pass();
    }

}

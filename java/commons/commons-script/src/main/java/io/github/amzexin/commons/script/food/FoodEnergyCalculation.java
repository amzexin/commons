package io.github.amzexin.commons.script.food;

import io.github.amzexin.commons.util.io.FileUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Description: 饮食能量摄入计算
 *
 * @author Lizexin
 * @date 2022-05-16 12:44
 */
public class FoodEnergyCalculation {

    private static String loadFile() throws IOException {
        InputStream is = FileUtils.getInputStream("commons-test/src/main/java/io/github/amzexin/commons/test/food/FoodEnergyCalculationTextTemplate.txt");

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        StringBuilder sb = new StringBuilder();

        String str;
        while ((str = br.readLine()) != null) {
            sb.append(str);
            sb.append("\n");
        }

        br.close();
        is.close();

        return sb.toString();
    }

    /**
     * 计算每个人所需要的能量
     *
     * @param height                      身高（单位：cm）
     * @param weight                      体重（单位：kg）
     * @param perKilogramOfEnergyConsumed 活动量（每千克消耗的千卡数）
     * @return
     */
    private static String calculation(int height, int weight, int perKilogramOfEnergyConsumed) throws IOException {
        // 计算 BMI = 体重 / 身高（单位：m）的平方
        double bmi = weight / Math.pow(height / 100.0D, 2.0);

        // BMI大于等于25.0属于偏胖体质
        boolean littleOverweight = bmi >= 25.0;

        // 根据体质，重新设置活动量
        // 偏胖体质在计算能量的时候减少5千卡
        int realPerKilogramOfEnergyConsumed = littleOverweight ? perKilogramOfEnergyConsumed - 5 : perKilogramOfEnergyConsumed;

        // 计算标准体重（单位：kg） = 身高（单位：cm） - 105
        int standardWeight = height - 105;

        // 计算每日所需的总能量（单位：千卡）
        int dailyTotalEnergy = standardWeight * realPerKilogramOfEnergyConsumed;

        /**
         * 计算三大能量所需的千卡数
         * 蛋白质：10% ~ 15%；建议：15%
         * 脂类：20% ~ 30%；建议30%
         * 碳水化合物：55% ~ 65%；建议55%
         */
        float proteinEnergy = dailyTotalEnergy * (15 / 100.0F);
        float maxProteinEnergy = dailyTotalEnergy * (20 / 100.0F);
        float lipidEnergy = dailyTotalEnergy * (30 / 100.0F);
        float carbohydrateEnergy = dailyTotalEnergy * (55 / 100.0F);

        // 计算需要摄入多少克的蛋白质； 1克蛋白质 = 4千卡能量
        float proteinWeight = proteinEnergy / 4;

        // 蛋白质的摄入量不能打折扣，鼓励尽量多一些，但不要超过总能量的20%。
        float maxProteinWeight = maxProteinEnergy / 4;

        // 计算需要摄入多少克的脂肪； 1克脂肪 = 9千卡能量
        float lipidWeight = lipidEnergy / 9;

        // 计算需要摄入多少克的碳水化合物； 1克碳水化合物 = 4千卡能量
        float carbohydrateWeight = carbohydrateEnergy / 4;

        String text = loadFile();
        text = text.replace("{height}", height + "");
        text = text.replace("{weight}", weight + "");
        text = text.replace("{bmi}", bmi + "");
        text = text.replace("{littleOverweight}", littleOverweight ? "是" : "否");
        text = text.replace("{standardWeight}", standardWeight + "");
        text = text.replace("{perKilogramOfEnergyConsumed}", perKilogramOfEnergyConsumed + "");
        text = text.replace("{proteinWeight}", proteinWeight + "");
        text = text.replace("{maxProteinWeight}", maxProteinWeight + "");
        text = text.replace("{lipidWeight}", lipidWeight + "");
        text = text.replace("{carbohydrateWeight}", carbohydrateWeight + "");
        return text;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(calculation(158, 65, 40));
    }
}

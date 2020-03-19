package com.cjyc.server.pay;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class test {



    public static void main(String[] args) {

        List<Car> list = Lists.newArrayList();
        for(int i = 0 ; i < 8 ; i++ ){
            Car car = new Car();
            car.setCarNo("D20200319-00" + i);
            car.setLineFee(new BigDecimal(400));
            list.add(car);
        }
        computeUnabsoluteAvg(new BigDecimal(3061), list);

    }

    private static void computeUnabsoluteAvg(BigDecimal total, List<Car> args) {

        if(total == null || CollectionUtils.isEmpty(args)){
            return;
        }

        //求线路费用和
        BigDecimal lineFeeSum = args.stream().map(Car::getLineFee).reduce(BigDecimal.ZERO, BigDecimal::add);
        //车辆实际费用（保留精度后）之和
        BigDecimal carFeeSum = BigDecimal.ZERO;
        for (Car car : args) {
            BigDecimal ratio = BigDecimal.ZERO;
            if(car.getLineFee() != null){
                //车辆实际费用 = 车辆线路费用 X 输入总费用 / 线路费用之和，保留精度，后位舍弃
                ratio = car.getLineFee().multiply(total).divide(lineFeeSum, 2, RoundingMode.DOWN);
            }
            System.out.println(car.getCarNo() + ":" + ratio);
            car.setCarFee(ratio);
            //求和
            carFeeSum = carFeeSum.add(ratio);

        }

        System.out.println("---------------------------------------");
        BigDecimal oneFen = BigDecimal.ONE.divide(new BigDecimal(100), 2, RoundingMode.DOWN);
        BigDecimal cha = total.subtract(carFeeSum);
        //总费用 与 车辆实际费用 差值
        if(BigDecimal.ZERO.compareTo(cha) != 0){
            //不能均分
            for (Car car : args) {
                //运费
                if (cha.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }
                car.setCarFee(car.getCarFee().add(oneFen));
                cha = cha.subtract(oneFen);
                System.out.println(car.getCarNo() + ":" + car.getCarFee());
            }
        }

    }







}


class Car{
    private String carNo;
    private BigDecimal lineFee;
    private BigDecimal carFee;

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public BigDecimal getLineFee() {
        return lineFee;
    }

    public void setLineFee(BigDecimal lineFee) {
        this.lineFee = lineFee;
    }

    public BigDecimal getCarFee() {
        return carFee;
    }

    public void setCarFee(BigDecimal carFee) {
        this.carFee = carFee;
    }
}

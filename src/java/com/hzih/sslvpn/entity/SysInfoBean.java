package com.hzih.sslvpn.entity;

/**
 * Created by 钱晓盼 on 15-6-11.
 */
public class SysInfoBean {
    /*
    top -b -n 1
        top - 11:03:09 up 20 days, 17:54,  1 user,  load average: 1.63, 1.71, 1.67
        Tasks:  82 total,   2 running,  80 sleeping,   0 stopped,   0 zombie
        %Cpu(s):  0.9 us,  2.1 sy,  0.0 ni, 97.0 id,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st
        KiB Mem:   3924976 total,  1684376 used,  2240600 free,    93292 buffers
        KiB Swap:   212988 total,        0 used,   212988 free,   820676 cached
     */
    private String sysTime;//11:03:09 — 当前系统时间
    private String sysUpTime;//    up 20 days,17:54 系统已经运行了20天17小时54分钟（在这期间没有重启过）
    private  int users;//1 users — 当前有1个用户登录系统
    private double loadAverage_1;//load average: 1.63, 1.71, 1.67 — load average后面的三个数分别是1分钟、5分钟、15分钟的负载情况。
    private double loadAverage_5;
    private double loadAverage_15;

    private int taskTotal;// 82 total 系统现在共有82个进程
    private int taskRunning;// 2 running  进程处于运行中的有1个
    private int taskSleeping;// 80 sleeping  80个在休眠（sleep）
    private int taskStopped;//  0 stopped   stopped状态的有0个
    private int taskZombie;//  0 zombie zombie状态（僵尸）的有0个。


    private double cpuUserUse;//6.7% us — 用户空间占用CPU的百分比。
    private double cpuSysUse; //0.4% sy — 内核空间占用CPU的百分比。
    private double cpuNiceUse;//0.0% ni — 改变过优先级的进程占用CPU的百分比
    private double cpuIdleUse; //92.9% id — 空闲CPU百分比
    private double cpuIoUse;//0.0% wa — IO等待占用CPU的百分比
    private double cpuHiUse; //0.0% hi — 硬中断（Hardware IRQ）占用CPU的百分比
    private double cpuSiUse; //0.0% si — 软中断（Software Interrupts）占用CPU的百分比

    private double memTotal;//8306544k total — 物理内存总量（8GB）
    private double memUse; //7775876k used — 使用中的内存总量（7.7GB）   现在系统内核控制的内存数
    private double memFree; //530668k free — 空闲内存总量（530M）
    private double menBuffers;//79236k buffers — 缓存的内存量 （79M）
    private double memCanBeUse; //  可用内存 = memFree + memBuffers + swapCached

    private double swapTotal; //2031608k total — 交换区总量（2GB）
    private double swapUsed; //2556k used — 使用的交换区总量（2.5M）     不断变化说明内存不够用
    private double swapFree; //2029052k free — 空闲交换区总量（2GB）
    private double swapCached;//4231276k cached — 缓冲的交换区总量（4GB）

    private double inAll;  //所有网卡接收总流量
    private double outAll; //所有网卡发送总流量
    private double inFlux; //所有网卡接收瞬时流量差
    private double outFlux; //所有网卡发送瞬时流量差
    private double inAverage; //所有网卡接收单位时间平均流量
    private double outAverage;//所有网卡发送单位时间平均流量


    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    public String getSysUpTime() {
        return sysUpTime;
    }

    public void setSysUpTime(String sysUpTime) {
        this.sysUpTime = sysUpTime;
    }


    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }

    public double getLoadAverage_1() {
        return loadAverage_1;
    }

    public void setLoadAverage_1(double loadAverage_1) {
        this.loadAverage_1 = loadAverage_1;
    }

    public double getLoadAverage_5() {
        return loadAverage_5;
    }

    public void setLoadAverage_5(double loadAverage_5) {
        this.loadAverage_5 = loadAverage_5;
    }

    public double getLoadAverage_15() {
        return loadAverage_15;
    }

    public void setLoadAverage_15(double loadAverage_15) {
        this.loadAverage_15 = loadAverage_15;
    }


    public int getTaskTotal() {
        return taskTotal;
    }

    public void setTaskTotal(int taskTotal) {
        this.taskTotal = taskTotal;
    }

    public int getTaskRunning() {
        return taskRunning;
    }

    public void setTaskRunning(int taskRunning) {
        this.taskRunning = taskRunning;
    }

    public int getTaskSleeping() {
        return taskSleeping;
    }

    public void setTaskSleeping(int taskSleeping) {
        this.taskSleeping = taskSleeping;
    }

    public int getTaskStopped() {
        return taskStopped;
    }

    public void setTaskStopped(int taskStopped) {
        this.taskStopped = taskStopped;
    }

    public int getTaskZombie() {
        return taskZombie;
    }

    public void setTaskZombie(int taskZombie) {
        this.taskZombie = taskZombie;
    }

    public double getCpuUserUse() {
        return cpuUserUse;
    }

    public void setCpuUserUse(double cpuUserUse) {
        this.cpuUserUse = cpuUserUse;
    }

    public double getCpuSysUse() {
        return cpuSysUse;
    }

    public void setCpuSysUse(double cpuSysUse) {
        this.cpuSysUse = cpuSysUse;
    }

    public double getCpuNiceUse() {
        return cpuNiceUse;
    }

    public void setCpuNiceUse(double cpuNiceUse) {
        this.cpuNiceUse = cpuNiceUse;
    }

    public double getCpuIdleUse() {
        return cpuIdleUse;
    }

    public void setCpuIdleUse(double cpuIdleUse) {
        this.cpuIdleUse = cpuIdleUse;
    }

    public double getCpuIoUse() {
        return cpuIoUse;
    }

    public void setCpuIoUse(double cpuIoUse) {
        this.cpuIoUse = cpuIoUse;
    }

    public double getCpuHiUse() {
        return cpuHiUse;
    }

    public void setCpuHiUse(double cpuHiUse) {
        this.cpuHiUse = cpuHiUse;
    }

    public double getCpuSiUse() {
        return cpuSiUse;
    }

    public void setCpuSiUse(double cpuSiUse) {
        this.cpuSiUse = cpuSiUse;
    }

    public double getMemTotal() {
        return memTotal;
    }

    public void setMemTotal(double memTotal) {
        this.memTotal = memTotal;
    }

    public double getMemUse() {
        return memUse;
    }

    public void setMemUse(double memUse) {
        this.memUse = memUse;
    }

    public double getMemFree() {
        return memFree;
    }

    public void setMemFree(double memFree) {
        this.memFree = memFree;
    }

    public double getMenBuffers() {
        return menBuffers;
    }

    public void setMenBuffers(double menBuffers) {
        this.menBuffers = menBuffers;
    }

    public double getMemCanBeUse() {
        return memCanBeUse;
    }

    public void setMemCanBeUse(double memCanBeUse) {
        this.memCanBeUse = memCanBeUse;
    }

    public double getSwapTotal() {
        return swapTotal;
    }

    public void setSwapTotal(double swapTotal) {
        this.swapTotal = swapTotal;
    }

    public double getSwapUsed() {
        return swapUsed;
    }

    public void setSwapUsed(double swapUsed) {
        this.swapUsed = swapUsed;
    }

    public double getSwapFree() {
        return swapFree;
    }

    public void setSwapFree(double swapFree) {
        this.swapFree = swapFree;
    }

    public double getSwapCached() {
        return swapCached;
    }

    public void setSwapCached(double swapCached) {
        this.swapCached = swapCached;
    }

    public double getInAll() {
        return inAll;
    }

    public void setInAll(double inAll) {
        this.inAll = inAll;
    }

    public double getOutAll() {
        return outAll;
    }

    public void setOutAll(double outAll) {
        this.outAll = outAll;
    }

    public double getInFlux() {
        return inFlux;
    }

    public void setInFlux(double inFlux) {
        this.inFlux = inFlux;
    }

    public double getOutFlux() {
        return outFlux;
    }

    public void setOutFlux(double outFlux) {
        this.outFlux = outFlux;
    }

    public double getInAverage() {
        return inAverage;
    }

    public void setInAverage(double inAverage) {
        this.inAverage = inAverage;
    }

    public double getOutAverage() {
        return outAverage;
    }

    public void setOutAverage(double outAverage) {
        this.outAverage = outAverage;
    }

}

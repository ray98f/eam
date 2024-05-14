# EAM（设备维修维护）后台服务

## **发布版本**

### ~~**v0.1.1**~~

~~初始开发完成分支，修复第一轮测试bug，现已删除~~

### **v0.2.1**

修复第二轮测试bug

完成第一次需求优化，优化内容包括

|  优化模块  | 优化内容                                                   | 完成状态 |
|:------:|:-------------------------------------------------------|:-----|
| 车辆维保台账 | 1.新增部分字段<br/>2.修改查询条件<br/>3.修改部分字段的枚举值<br/>4.普查与技改文件上传 | 已完成  |
| 计量器具管理 | 1.调整模块结构<br/>2.新增特种设备台账增删改查功能                          | 已完成  |
| 特种设备管理 | 1.调整模块结构<br/>2.新增计量器具台账增删改查功能                          | 已完成  |
|  故障管理  | 1.合并故障查询的部分字段<br/>2.优化故障查询相关功能                         | 已完成  |

### **v0.3.1**

修复第三轮测试bug

优化代码书写规范、优化代码结构

### **v0.3.2**

新增原检修工单模块手持端功能

1. 检修模块获取
2. 根据检修模块获取检修项
3. 检修项排查

### **v0.3.3**

根据业主方提供的需求及修改意见进行EAM优化版本

需求新增与优化包括以下部分：

|   模块   | 内容                                                 | 完成状态 |
|:------:|:---------------------------------------------------|:-----|
| 设备管理模块 | 1.设备移交流程优化<br/>2.车辆维保台账模块各菜单优化<br/>3.部分导出功能字段优化    | 已完成  |
| 预防性检修  | 1.检修模板模板导入<br/>2.检修计划、周计划优化<br/>3.检修工单解决工单编号不唯一的问题 | 已完成  |
|  统计页面  | easyexcel替换原始poi导出                                 | 已完成  |
|  故障查询  | 1.下发、派工逻辑优化<br/>2.添加报告（完工）功能                       | 已完成  |
|  故障提报  | 提报暂存草稿状态                                           | 已完成  |
|  基础管理  | 设备分类及工单触发规则优化                                      | 已完成  |

### **v0.4.1**

根据业主方提供的新需求与修改一键进行第二轮EAM功能优化与开发版本

同时进行 SonarQube Quality Gate 部分问题修复与优化

需求新增与优化包括以下部分：

|    模块     | 内容                                                                | 完成状态 |
|:---------:|:------------------------------------------------------------------|:-----|
| 每日列车里程及能耗 | 1.新增每日列车里程及能耗模块（包含增删改查）                                           | 已完成  |
|   故障提报    | 1.优化提报流程<br/>2.开放外部故障提报接口<br/>3.解决外部提报接口高并发                       | 已完成  |
|   故障查询    | 1.新增催办功能<br/>2.新增故障流转流程图                                          | 已完成  |
|   统计分析    | 新增中铁通故障查询与周报导出（与其他公司做区分）                                          | 已完成  |
|   预防性检修   | 预防性检修字段调整                                                         | 已完成  |
|   基础管理    | 1.优化工单触发规则字段<br/>2.新增开发Bom结构管理页面                                  | 已完成  |
|   检测管理    | 1.新增开发特种设备分类管理页面<br/>2.优化特种设备管理<br/>3.新增开发其他设备管理页面<br/>4.迁移计量器具管理 | 已完成  |

### **v0.4.2**

根据EAM系统统计分析模块梳理，修改或优化统计分析模块各模块内容

20240326演示前最终版本，优化并处理了目前已知可快速优化的所有问题

### **v1.0.0**

正式上线测试环境版本

### **v1.0.1**

持续优化版本

铁投测试环境版本目前仍然处于迭代优化阶段，目前已优化、新增功能点如下：

**优化功能**

- 优化每日走行里程台账每日自动生成
- 优化特种设备、其他设备检修有效期跟随类别周期修改而变化
- 外部系统推送故障取消自动派工，需要在派工时手动补充信息
- 统计分析相关细节优化
- 优化故障模块相关数据获取（工班长、工班人员等）

**新增功能**

- 新增检修计划（中车）排期功能
- 新增故障跟踪工单-跟踪报告相关功能
- 新增检修工单数据隔离

---
**_以下为待完成内容_**

|    模块     | 内容             |
|:---------:|:---------------|
|  报表导入导出   | 1.导入模板下载       |
| 每日列车里程及能耗 | 1.获取车辆PHM系统数据  |
|    其他     | 1.外部系统接入实现故障推送 |
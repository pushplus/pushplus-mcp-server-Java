# pushplus 推送加 MCP Server
官网：https://www.pushplus.plus

## 功能描述
pushplus(推送加)是一个集成了微信、短信、邮件、企业微信、、腾讯轻联、钉钉、飞书、bark、gotify、集简云等实时消息推送平台。只需要调用简单的API，即可帮您迅速完成消息的推送，使用简单方便。

## 使用方式
1. 依赖JDK21
2. 使用maven工具来构建项目，构建命令：mvn clean package
3. 在 target 目录中启动程序，命令：java -Dpushplus.token=替换为自己的消息token -Dlogging.pattern.console= -jar pushplus-mcp-1.0.0.jar
4. 启动命令中可带上-Dpushplus.token，用来固定发送消息的token，否则需要在提示词中指定

## Cursor中使用
在Cursor Settings中找到Tool & Integrations, 点击New MCP Server，填入一下配置：

```
{
  "mcpServers": {
    "pushplus-mcp-server": {
      "command": "java",
      "args": [
        "-Dpushplus.token=替换为自己的消息token",
        "-Dlogging.pattern.console=",
        "-jar",
        "yourFilePath\\pushplus-mcp-1.0.0.jar"
      ],
      "env": {}
    }
  }
}
```

其中 -Dpushplus.token= 后面替换为从pushplus官网上获取到的消息token或用户token。
yourFilePath\\pushplus-mcp-1.0.0.jar 修改为自己本地的文件路径。

## 演示效果
如图

![效果](images/image.png)
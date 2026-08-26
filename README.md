# pushplus mcp server
官网：https://www.pushplus.plus

## 功能描述
pushplus是一个集成了微信、QQ机器人、短信、邮件、企业微信、腾讯轻联、钉钉、飞书、bark、gotify、集简云等实时消息推送平台。

本项目基于 Spring AI MCP，通过 stdio 为 AI 大模型提供：
1. 消息发送（`/send`、`/batchSend`）
2. 全量开放接口（`/open/**`，约 79 个工具）

## 环境变量

| 变量 | 说明 |
|------|------|
| `PUSHPLUS_TOKEN` | 用户 token（发送与开放接口共用，必需） |
| `PUSHPLUS_SECRET_KEY` | 开放接口 secretKey（调用 open_* 时需要） |
| `PUSHPLUS_BASE_URL` | API 根地址，默认 `https://www.pushplus.plus` |
| `PUSHPLUS_OPEN_API_PREFIX` | 开放接口前缀，默认 `/api`（发送接口不走此前缀） |

说明：
- `PUSHPLUS_TOKEN` 同时用于消息发送和 `getAccessKey` 换取 access-key（须为**用户 token**，不支持消息 token）
- 开放接口实际地址形如 `https://www.pushplus.plus/api/open/...`；发送接口仍为 `https://www.pushplus.plus/send`
- 使用开放接口前请在官网开启开放接口，并配置 `secretKey` 与安全 IP
- 各 `open*` 工具的请求/响应字段说明对齐官方文档：https://www.pushplus.plus/doc/guide/openApi.html

## MCP 工具

### 发送
| 工具 | 对应接口 |
|------|----------|
| `send` | `POST /send` |
| `batchSend` | `POST /batchSend` |

### 开放接口（命名：`openXxx`）
覆盖模块：auth / user / message / token / topic / topicUser / friend / webhook / setting / pre / mail / mp / cp / clawBot / qqBot / file / userImage / pay。

常用示例：
- `openGetAccessKey`：换取 access-key（排查用）
- `openUserMyInfo`：个人资料
- `openMessageSendResult`：按 shortCode 查发送结果
- `openWebhookList` / `openTopicList`：配置查询
- `openQqBotGetBindLink` / `openQqBotInfo` / `openQqBotGroupList` / `openQqBotAdd`：QQ 机器人绑定与群配置

破坏性操作（删除/提现/解绑等）已在 description 中标注「高风险」。

## 使用方式
1. 依赖 JDK 21
2. 构建：`mvn clean package`
3. 启动：

```bash
PUSHPLUS_TOKEN=你的token \
PUSHPLUS_SECRET_KEY=你的secretKey \
java -Dlogging.pattern.console= -jar pushplus-mcp-1.0.7.jar
```

## Cursor 中使用

```json
{
  "mcpServers": {
    "pushplus-mcp-server": {
      "command": "java",
      "args": [
        "-Dlogging.pattern.console=",
        "-jar",
        "/path/to/pushplus-mcp-1.0.7.jar"
      ],
      "env": {
        "PUSHPLUS_TOKEN": "替换为自己的token",
        "PUSHPLUS_SECRET_KEY": "替换为secretKey"
      }
    }
  }
}
```

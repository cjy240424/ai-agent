package com.cjy.record;

import dev.langchain4j.model.output.structured.Description;

public enum IntentType {
    @Description("物流与订单状态查询，例如：快递到哪了、查单号、孙悟空派件")
    LOGISTICS,

    @Description("退换货与售后服务，例如：申请退款、商品破损、退货理由")
    REFUND,

    @Description("公司内部规章制度，例如：年假怎么休、HR政策、行政规定")
    POLICY,

    @Description("闲聊、打招呼或其他无法识别的意图")
    UNKNOWN
}

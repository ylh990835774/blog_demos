package com.bolingcavalry.service;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * @author will (zq2599@gmail.com)
 * @version 1.0
 * @description: 产生事件
 * @date 2021/5/23 14:48
 */
public class StringEventProducer {

    // 存储数据的环形队列
    private final RingBuffer<StringEvent> ringBuffer;

    public StringEventProducer(RingBuffer<StringEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(String content) {

        // ringBuffer是个队列，其next方法返回的是下最后一条记录之后的位置，这是个可用位置
        long sequence = ringBuffer.next();

        try {
            // sequence位置取出的事件是空事件
            StringEvent stringEvent = ringBuffer.get(sequence);
            // 空事件添加业务信息
            stringEvent.setValue(content);
        } finally {
            // 发布
            ringBuffer.publish(sequence);
        }
    }

}


/*
import com.lmax.disruptor.RingBuffer;

public class LongEventProducer
{
    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer)
    {
        this.ringBuffer = ringBuffer;
    }

    public void onData(ByteBuffer bb)
    {
        long sequence = ringBuffer.next();  // Grab the next sequence
        try
        {
            LongEvent event = ringBuffer.get(sequence); // Get the entry in the Disruptor
            // for the sequence
            event.set(bb.getLong(0));  // Fill with data
        }
        finally
        {
            ringBuffer.publish(sequence);
        }
    }
}
*/
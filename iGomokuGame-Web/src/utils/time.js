export const timestampToTime = (time) => {
    const localTime = time.toLocaleString()
    return localTime.split('/').join('-')
}

const oneYearMilliseconds = 31536000000 // 365天
const oneMonthMilliseconds = 2592000000 // 30天
const oneDayMilliseconds = 86400000 // 24小时
const oneHourMilliseconds = 3600000 // 60分钟
const oneMinuteMilliseconds = 60000 // 1分钟

export const timeToText = (time) => {
    // time: '2023-12-18T20:00:23'
    // console.log(time);
    var temp = time.split('T')
    var tempLeft = temp[0].split('-'), tempRight = temp[1].split(':')
    var year = tempLeft[0]
    var month = +tempLeft[1]
    var day = tempLeft[2]
    var hours = tempRight[0]
    var minutes = tempRight[1]
    var seconds = tempRight[2]
    var milliseconds = '0'
    var lastTime = new Date(year, month - 1, day, hours, minutes, seconds, milliseconds)
    var timestamp = Date.now() - lastTime.getTime() // 上次在线时间距离现在时间的时间戳
    if (timestamp / oneMinuteMilliseconds < 5){
        return '刚刚'
    }else if (timestamp / oneMinuteMilliseconds  < 60){
        return Math.trunc(timestamp / oneMinuteMilliseconds) + '分钟前'
    }else if (timestamp / oneHourMilliseconds < 24){
        return Math.trunc(timestamp / oneHourMilliseconds) + '小时前'
    }else if (timestamp / oneDayMilliseconds < 30){
        return Math.trunc(timestamp / oneDayMilliseconds) + '天前'
    }else if (timestamp / oneMonthMilliseconds < 12){
        return Math.trunc(timestamp / oneMonthMilliseconds) + '月前'
    }else if (timestamp / oneYearMilliseconds < 10){
        return Math.trunc(timestamp / oneYearMilliseconds) + '年前'
    }else{
        return '超过10年没上线了...'
    }
}
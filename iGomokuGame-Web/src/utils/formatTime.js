// 格式化时间，将时间转化成更加友好的格式
export const formatDateTime = (isoString) => {
  let date = new Date(isoString)
  return (
    date.getFullYear() +
    '-' +
    String(date.getMonth() + 1).padStart(2, '0') +
    '-' +
    String(date.getDate()).padStart(2, '0') +
    ' ' +
    String(date.getHours()).padStart(2, '0') +
    ':' +
    String(date.getMinutes()).padStart(2, '0') +
    ':' +
    String(date.getSeconds()).padStart(2, '0')
  )
}

export const reverseFormatDateTime = (customFormattedString) => {
  const parts = customFormattedString.split(' ')
  if (parts.length !== 2) {
    // 格式不正确，可以抛出错误或返回原始字符串
    throw new Error('Invalid date time format')
  }
  const datePart = parts[0] // 日期部分（"2023-12-19"）
  const timePart = parts[1] // 时间部分（"12:41:43"）
  return `${datePart}T${timePart}`
}

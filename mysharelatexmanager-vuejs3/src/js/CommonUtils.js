import moment from 'moment'

function displayDate(
  value,
  defaultValue = 'No Value',
  fmt = 'YYYY/MM/DD hh:mm:ss'
) {
  return value == null ? defaultValue : moment(value).format(fmt)
}

export default {
  displayDate
}

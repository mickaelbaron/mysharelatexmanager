import { FilterService } from 'primevue/api'
import CommonUtils from './CommonUtils.js'

const DATE_FILTER = 'dateFilter'
const GLOBAL_FILTER = 'globalFilter'
const OBJECT_FILTER = 'objectFilter'

FilterService.register(DATE_FILTER, (value, filter) => {
  if (filter === undefined || filter === null || filter.trim() === '') {
    return true
  }

  if (value === undefined || value === null) {
    return false
  }

  // Only Last Updated is an number type
  if (typeof value === 'number') {
    let result = CommonUtils.displayDate(value)

    return result.toString().includes(filter.toString())
  }

  return value.toString().includes(filter.toString())
})

FilterService.register(GLOBAL_FILTER, (value, filter) => {
  if (filter === undefined || filter === null || filter.trim() === '') {
    return true
  }

  if (value === undefined || value === null) {
    return false
  }

  // Only Collaborators is an object type
  if (typeof value === 'object') {
    let result = value.find((element) => element.email.includes(filter))
    return result !== undefined
  }

  return value.toString().includes(filter.toString())
})

FilterService.register(OBJECT_FILTER, (value, filter) => {
  if (filter === undefined || filter === null || filter.trim() === '') {
    return true
  }

  if (value === undefined || value === null) {
    return false
  }

  // Only Collaborators is an object type
  if (typeof value === 'object') {
    let result = value.find((element) => element.email.includes(filter))
    return result !== undefined
  }

  return value.toString().includes(filter.toString())
})

export { DATE_FILTER, GLOBAL_FILTER, OBJECT_FILTER }

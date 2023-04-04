<script setup>
import { ref, onMounted, watch, inject } from 'vue'
import { FilterService, FilterMatchMode } from 'primevue/api'
import TriStateCheckbox from 'primevue/tristatecheckbox'

import DataTable from 'primevue/datatable'
import Column from 'primevue/column'

import DetailProjectRow from './DetailProjectRow.vue'
import DetailProjectDialog from './DetailProjectDialog.vue'
import GlobalService from '../js/GlobalService.js'
import CommonUtils from '../js/CommonUtils.js'

const allProjects = ref([])
const expandedRows = ref([])

const errorState = inject('ErrorState')

const props = defineProps({
  searchContent: {
    type: String,
    required: true,
    default: ''
  }
})
const detailProjectDialogVisible = ref(false)
const loading = ref(true)
const globalFiler = ref('global filter')
const objectFilter = ref('object filter')
const dateFilter = ref('date filter')
const globalFilter = ref({
  global: { value: null, matchMode: globalFiler.value },
  id: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  name: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  owner: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  active: { value: null, matchMode: FilterMatchMode.EQUALS },
  collaberators: { value: null, matchMode: objectFilter.value },
  lastUpdated: { value: null, matchMode: dateFilter.value }
})

const currentProjectIdEdit = ref('')

onMounted(() => {
  loading.value = true
  GlobalService.getProjects(onSuccessProjects, onErrorProjects)

  FilterService.register(objectFilter.value, (value, filter) => {
    if (filter === undefined || filter === null || filter.trim() === '') {
      return true
    }

    if (value === undefined || value === null) {
      return false
    }

    // Only Collaberators is an object type
    if (typeof value === 'object') {
      let result = value.find((element) => element.email.includes(filter))
      return result !== undefined
    }

    return value.toString().includes(filter.toString())
  })

  FilterService.register(dateFilter.value, (value, filter) => {
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

  FilterService.register(globalFiler.value, (value, filter) => {
    if (filter === undefined || filter === null || filter.trim() === '') {
      return true
    }

    if (value === undefined || value === null) {
      return false
    }

    // Only Collaberators is an object type
    if (typeof value === 'object') {
      let result = value.find((element) => element.email.includes(filter))
      return result !== undefined
    }

    return value.toString().includes(filter.toString())
  })
})

function openProjectDetailDialog(data) {
  currentProjectIdEdit.value = data.id
  detailProjectDialogVisible.value = true
}

function onSuccessProjects(data) {
  allProjects.value = data.data
  loading.value = false
}

function onErrorProjects(error) {
  errorState.methods.setErrorCode(error)
}

function saveNewProjectData(data) {
  if (data) {
    const objectToReplace = allProjects.value.find(
      (currentUser) => currentUser.id === data.id
    )

    if (objectToReplace) {
      Object.assign(objectToReplace, data)
    }
  }
}

watch(
  () => props.searchContent,
  (searchContent) => {
    globalFilter.value.global.value = searchContent
  }
)
</script>

<template>
  <DetailProjectDialog
    v-model:visible="detailProjectDialogVisible"
    :project-id="currentProjectIdEdit"
    @save-new-projectdata="saveNewProjectData"
  />

  <DataTable
    v-model:expandedRows="expandedRows"
    v-model:filters="globalFilter"
    filter-display="row"
    :paginator="true"
    :rows="100"
    :value="allProjects"
    :rows-per-page-options="[10, 20, 50, 100]"
    responsive-layout="scroll"
    current-page-report-template="Showing {first} to {last} of {totalRecords}"
    paginator-template="CurrentPageReport FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown"
    :global-filter-fields="['id', 'name', 'owner', 'collaberators']"
    :loading="loading"
  >
    <template #loading>Loading customers data. Please wait.</template>
    <Column :expander="true" header-style="width: 3rem" />
    <Column field="id" :sortable="true" header="Id">
      <template #filter="{ filterModel, filterCallback }">
        <input
          v-model="filterModel.value"
          type="text"
          class="form-control"
          :placeholder="`Search by Id - ${filterModel.matchMode}`"
          aria-label="id"
          aria-describedby="basic-addon1"
          @change="filterCallback()"
        />
      </template>
    </Column>
    <Column field="name" :sortable="true" header="Name">
      <template #filter="{ filterModel, filterCallback }">
        <input
          v-model="filterModel.value"
          type="text"
          class="form-control"
          :placeholder="`Search by Name - ${filterModel.matchMode}`"
          aria-label="name"
          aria-describedby="basic-addon1"
          @change="filterCallback()"
        />
      </template>
    </Column>
    <Column field="owner" :sortable="true" header="Owner">
      <template #filter="{ filterModel, filterCallback }">
        <input
          v-model="filterModel.value"
          type="text"
          class="form-control"
          :placeholder="`Search by Owner - ${filterModel.matchMode}`"
          aria-label="Owner"
          aria-describedby="basic-addon1"
          @change="filterCallback()"
        />
      </template>
    </Column>
    <Column
      field="active"
      header="Active?"
      data-type="boolean"
      :sortable="true"
    >
      <template #body="slotProps">
        <span v-if="slotProps.data.active">
          <i class="bi bi-check" style="font-size: 1.5rem"></i
        ></span>
        <span v-else-if="slotProps.data.active === false">
          <i class="bi bi-x" style="font-size: 1.5rem"></i>
        </span>
        <span v-else>
          <i class="bi bi-question" style="font-size: 1.5rem"></i>
        </span>
      </template>
      <template #filter="{ filterModel, filterCallback }">
        <TriStateCheckbox
          id="flexCheckDefault"
          v-model="filterModel.value"
          class="form-check-input"
          type="checkbox"
          @change="filterCallback()"
        />
      </template>
    </Column>
    <Column
      field="collaberators"
      header="Collaberators"
      :show-filter-menu="false"
      :sortable="true"
    >
      <template #body="slotProps">
        <ul>
          <li v-for="current in slotProps.data.collaberators" :key="current.id">
            {{ current.email }}
          </li>
        </ul>
      </template>
      <template #filter="{ filterModel, filterCallback }">
        <input
          v-model="filterModel.value"
          type="text"
          class="form-control"
          placeholder="Search by Collaberators"
          aria-label="Collaberators"
          aria-describedby="basic-addon1"
          @change="filterCallback()"
        />
      </template>
    </Column>
    <Column
      field="lastUpdated"
      header="Last Updated"
      :show-filter-menu="false"
      :sortable="true"
    >
      <template #body="slotProps">{{
        CommonUtils.displayDate(slotProps.data.lastUpdated)
      }}</template>
      <template #filter="{ filterModel, filterCallback }">
        <input
          v-model="filterModel.value"
          type="text"
          class="form-control"
          placeholder="YYYY/MM/D HH:mm:ss"
          aria-label="lastUpdated"
          aria-describedby="basic-addon1"
          @change="filterCallback()"
        />
      </template>
    </Column>
    <Column style="min-width: 8rem">
      <template #body="slotProps">
        <div class="d-flex flex-nowrap">
          <button
            type="button"
            class="btn btn-outline-primary"
            :title="'Edit the project ' + slotProps.data.name"
            @click="openProjectDetailDialog(slotProps.data)"
          >
            <i class="bi bi-pencil"></i>
          </button>
          <!--<button
            type="button"
            class="btn btn-outline-primary ms-2"
            :title="'Delete the project ' + slotProps.data.name"
            @click="openDeleteProjectDialog(slotProps.data)"
          >
            <i class="bi bi-trash"></i>
          </button> -->
        </div>
      </template>
    </Column>
    <template #expansion="slotProps">
      <DetailProjectRow :data="slotProps.data" />
    </template>
  </DataTable>
</template>

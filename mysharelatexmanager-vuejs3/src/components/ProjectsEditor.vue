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

import { DATE_FILTER, GLOBAL_FILTER, OBJECT_FILTER } from '../js/RegisterFilters.js'

const allProjects = ref([])
const expandedRows = ref([])

const errorState = inject('ErrorState')

const props = defineProps({
  searchContent: {
    type: String,
    required: true,
  },
})

const createFilter = (matchMode) => ({
  value: null,
  matchMode,
})

const detailProjectDialogVisible = ref(false)
const loading = ref(true)
const globalFilter = ref({
  global: createFilter(GLOBAL_FILTER),
  id: createFilter(FilterMatchMode.STARTS_WITH),
  name: createFilter(FilterMatchMode.STARTS_WITH),
  owner: createFilter(FilterMatchMode.STARTS_WITH),
  active: createFilter(FilterMatchMode.EQUALS),
  collaborators: createFilter(OBJECT_FILTER),
  lastUpdated: createFilter(DATE_FILTER),
})

const currentProjectIdEdit = ref('')

onMounted(() => {
  GlobalService.getProjects(onSuccessProjects, onErrorProjects)
})

function openProjectDetailDialog(project) {
  currentProjectIdEdit.value = project.id
  detailProjectDialogVisible.value = true
}

function finishLoading() {
  loading.value = false
}

function onSuccessProjects(project) {
  allProjects.value = project.data
  finishLoading()
}

function onErrorProjects(error) {
  errorState.methods.setErrorCode(error)
  finishLoading()
}

function saveNewProjectData(data) {
  if (data) {
    const project = allProjects.value.find((project) => project.id === data.id)

    if (project) {
      Object.assign(project, data)
    }
  }
}

watch(
  () => props.searchContent,
  (value) => {
    globalFilter.value.global.value = value
  },
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
    :global-filter-fields="['id', 'name', 'owner', 'collaborators']"
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
          @change="filterCallback"
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
          @change="filterCallback"
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
          @change="filterCallback"
        />
      </template>
    </Column>
    <Column field="active" header="Active?" data-type="boolean" :sortable="true">
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
          @change="filterCallback"
        />
      </template>
    </Column>
    <Column field="collaborators" header="Collaborators" :show-filter-menu="false" :sortable="true">
      <template #body="slotProps">
        <ul>
          <li v-for="current in slotProps.data.collaborators" :key="current.id">
            {{ current.email }}
          </li>
        </ul>
      </template>
      <template #filter="{ filterModel, filterCallback }">
        <input
          v-model="filterModel.value"
          type="text"
          class="form-control"
          placeholder="Search by Collaborators"
          aria-label="Collaborators"
          aria-describedby="basic-addon1"
          @change="filterCallback"
        />
      </template>
    </Column>
    <Column field="lastUpdated" header="Last Updated" :show-filter-menu="false" :sortable="true">
      <template #body="slotProps">{{
        CommonUtils.displayDate(slotProps.data.last_updated)
      }}</template>
      <template #filter="{ filterModel, filterCallback }">
        <input
          v-model="filterModel.value"
          type="text"
          class="form-control"
          placeholder="YYYY/MM/D HH:mm:ss"
          aria-label="lastUpdated"
          aria-describedby="basic-addon1"
          @change="filterCallback"
        />
      </template>
    </Column>
    <Column style="min-width: 8rem">
      <template #body="slotProps">
        <div class="d-flex flex-nowrap">
          <button
            type="button"
            class="btn btn-outline-primary"
            :title="`Edit the project ${slotProps.data.name}`"
            @click="openProjectDetailDialog(slotProps.data)"
          >
            <i class="bi bi-pencil"></i>
          </button>
        </div>
      </template>
    </Column>
    <template #expansion="slotProps">
      <DetailProjectRow :data="slotProps.data" />
    </template>
  </DataTable>
</template>

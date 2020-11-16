<template>
  <v-container>
    <div v-for="(section, key) in page.sections" :key="key">
      <sectionBuilder :section="section" />
    </div>
  </v-container>
</template>

<script>
import sectionBuilder from './SectionComponent'
export default {
  name: 'pageBuilder',
  components: {
    sectionBuilder,
  },
  props: ['page'],
}
</script>

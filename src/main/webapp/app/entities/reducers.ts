import analyzer from 'app/entities/analyzer/analyzer.reducer';
import comment from 'app/entities/comment/comment.reducer';
import content from 'app/entities/content/content.reducer';
import contentFragment from 'app/entities/content-fragment/content-fragment.reducer';
import context from 'app/entities/context/context.reducer';
import organization from 'app/entities/organization/organization.reducer';
import search from 'app/entities/search/search.reducer';
import searchConfiguration from 'app/entities/search-configuration/search-configuration.reducer';
import searchResult from 'app/entities/search-result/search-result.reducer';
import tag from 'app/entities/tag/tag.reducer';
import thingType from 'app/entities/thing-type/thing-type.reducer';
import topic from 'app/entities/topic/topic.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  analyzer,
  comment,
  content,
  contentFragment,
  context,
  organization,
  search,
  searchConfiguration,
  searchResult,
  tag,
  thingType,
  topic,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;

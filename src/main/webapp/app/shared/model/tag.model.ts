import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IAnalyzer } from 'app/shared/model/analyzer.model';
import { IContent } from 'app/shared/model/content.model';
import { IContentFragment } from 'app/shared/model/content-fragment.model';
import { IContext } from 'app/shared/model/context.model';
import { IOrganization } from 'app/shared/model/organization.model';
import { ISearch } from 'app/shared/model/search.model';
import { ISearchConfiguration } from 'app/shared/model/search-configuration.model';
import { ISearchResult } from 'app/shared/model/search-result.model';
import { IThingType } from 'app/shared/model/thing-type.model';
import { ITopic } from 'app/shared/model/topic.model';

export interface ITag {
  id?: number;
  label?: string;
  description?: string | null;
  defaultTag?: boolean | null;
  dateCreated?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  createdBy?: IUser | null;
  analyzer?: IAnalyzer | null;
  content?: IContent | null;
  contentFragment?: IContentFragment | null;
  context?: IContext | null;
  organization?: IOrganization | null;
  search?: ISearch | null;
  searchConfiguration?: ISearchConfiguration | null;
  searchResult?: ISearchResult | null;
  thingType?: IThingType | null;
  topic?: ITopic | null;
}

export const defaultValue: Readonly<ITag> = {
  defaultTag: false,
};

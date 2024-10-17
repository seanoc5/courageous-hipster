import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IContent } from 'app/shared/model/content.model';
import { IContentFragment } from 'app/shared/model/content-fragment.model';
import { IContext } from 'app/shared/model/context.model';
import { IOrganization } from 'app/shared/model/organization.model';
import { ISearch } from 'app/shared/model/search.model';
import { ISearchConfiguration } from 'app/shared/model/search-configuration.model';
import { ISearchResult } from 'app/shared/model/search-result.model';
import { ITag } from 'app/shared/model/tag.model';
import { IThingType } from 'app/shared/model/thing-type.model';
import { ITopic } from 'app/shared/model/topic.model';

export interface IComment {
  id?: number;
  label?: string;
  description?: string | null;
  url?: string | null;
  dateCreated?: dayjs.Dayjs | null;
  lastUpdate?: dayjs.Dayjs | null;
  user?: IUser | null;
  content?: IContent | null;
  contentFragment?: IContentFragment | null;
  context?: IContext | null;
  organization?: IOrganization | null;
  search?: ISearch | null;
  searchConfiguration?: ISearchConfiguration | null;
  searchResult?: ISearchResult | null;
  tag?: ITag | null;
  thingType?: IThingType | null;
  topic?: ITopic | null;
}

export const defaultValue: Readonly<IComment> = {};

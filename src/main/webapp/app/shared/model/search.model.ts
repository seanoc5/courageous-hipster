import dayjs from 'dayjs';
import { ISearchConfiguration } from 'app/shared/model/search-configuration.model';
import { IUser } from 'app/shared/model/user.model';
import { IContext } from 'app/shared/model/context.model';
import { IThingType } from 'app/shared/model/thing-type.model';
import { ITag } from './tag.model';

export interface ISearch {
  id?: number;
  query?: string;
  additionalParams?: string | null;
  dateCreated?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  configurations?: Array<ISearchConfiguration> | ISearchConfiguration | null; // TODO: Remove ISearchConfiguration after array integration
  createdBy?: IUser | null;
  context?: IContext | null;
  type?: IThingType | null;
  tags?: Array<ITag> | null;
}

export const defaultValue: Readonly<ISearch> = {};

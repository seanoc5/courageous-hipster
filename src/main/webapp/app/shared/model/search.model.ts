import dayjs from 'dayjs';
import { ISearchConfiguration } from 'app/shared/model/search-configuration.model';
import { IUser } from 'app/shared/model/user.model';
import { IContext } from 'app/shared/model/context.model';
import { IThingType } from 'app/shared/model/thing-type.model';

export interface ISearch {
  id?: number;
  query?: string;
  additionalParams?: string | null;
  dateCreated?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  configuration?: ISearchConfiguration | null;
  createdBy?: IUser | null;
  context?: IContext | null;
  type?: IThingType | null;
}

export const defaultValue: Readonly<ISearch> = {};

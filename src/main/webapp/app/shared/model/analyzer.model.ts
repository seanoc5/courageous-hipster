import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IContext } from 'app/shared/model/context.model';
import { ISearchConfiguration } from 'app/shared/model/search-configuration.model';
import { ISearchResult } from 'app/shared/model/search-result.model';

export interface IAnalyzer {
  id?: number;
  label?: string;
  description?: string | null;
  code?: string | null;
  dateCreated?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  createdBy?: IUser | null;
  context?: IContext | null;
  searchConfiguration?: ISearchConfiguration | null;
  searchResult?: ISearchResult | null;
}

export const defaultValue: Readonly<IAnalyzer> = {};

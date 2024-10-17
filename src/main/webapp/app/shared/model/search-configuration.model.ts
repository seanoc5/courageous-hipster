import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface ISearchConfiguration {
  id?: number;
  label?: string;
  description?: string | null;
  defaultConfig?: boolean | null;
  url?: string | null;
  paramsJson?: string | null;
  headersJson?: string | null;
  dateCreated?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  createdBy?: IUser | null;
}

export const defaultValue: Readonly<ISearchConfiguration> = {
  defaultConfig: false,
};

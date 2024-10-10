import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface ITopic {
  id?: number;
  label?: string | null;
  description?: string | null;
  defaultTopic?: boolean | null;
  level?: number | null;
  dateCreated?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  createdBy?: IUser | null;
}

export const defaultValue: Readonly<ITopic> = {
  defaultTopic: false,
};

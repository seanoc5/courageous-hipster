import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IOrganization } from 'app/shared/model/organization.model';

export interface IContext {
  id?: number;
  label?: string;
  description?: string | null;
  level?: string | null;
  time?: string | null;
  location?: string | null;
  intent?: string | null;
  defaultContext?: boolean | null;
  dateCreated?: dayjs.Dayjs | null;
  lastUpdate?: dayjs.Dayjs | null;
  createdBy?: IUser | null;
  organization?: IOrganization | null;
}

export const defaultValue: Readonly<IContext> = {
  defaultContext: false,
};
